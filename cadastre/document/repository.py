# A repository handles submission and retrieval of documents against an
# underlying data store. A `Repository` wraps a specific SQLAlchemy Session, and
# should not outlive it.

import apistar.exceptions
from decorator import decorator
from sqlalchemy.orm.exc import NoResultFound

class RevisionNotFound(apistar.exceptions.NotFound):
    pass

class AnnotationNotFound(apistar.exceptions.NotFound):
    pass

class DocumentNotFound(apistar.exceptions.NotFound):
    pass

def sql_not_found_as(exc_type):
    @decorator
    def interceptor(f, *args, **kwargs):
        try:
            return f(*args, **kwargs)
        except NoResultFound as e:
            raise exc_type() from e
    return interceptor

class Repository(object):
    def __init__(self, session):
        self.session = session

    # Retrieves a previously-submitted revision from this repository. This is a
    # very straightforward SELECT by primary key, which either returns one row
    # or zero rows. If there is no result, this will raise a RevisionNotFound
    # error, which can be used directly as an HTTP response.
    @sql_not_found_as(RevisionNotFound)
    def retrieve_revision(self, message_id, revision):
        return self.session.query(Revision)\
            .filter_by(message_id = message_id, revision = revision)\
            .one()

    # Retrieves a previously-submitted annotation revision from this repository.
    # This is a very straightforward SELECT by primary key, which either returns
    # one row or zero rows. If there is no result, this will raise a
    # AnnotationNotFound error, which can be used directly as an HTTP response.
    @sql_not_found_as(AnnotationNotFound)
    def retrieve_annotation(self, message_id, revision):
        return self.session.query(Annotation)\
            .filter_by(message_id = message_id, revision = revision)\
            .one()

    # Retrieves a whole document from this repository. This is a select by
    # primary key, and therefore returns one or zero rows. If there is no
    # result, this will raise a DocumentNotFound error, which can be used
    # directly as an HTTP response.
    @sql_not_found_as(DocumentNotFound)
    def get_document(self, message_id):
        return self.session.query(Document)\
            .filter_by(message_id = message_id)\
            .one()

    # Returns a list of annotations stored in the repository, in order of the
    # dates of the associated messages. If `before` is set, only annotations
    # attached to documents whose dates are strictly before that date are
    # included in the result.
    def get_annotations(self, before = None):
        query = self.session.query(Annotation)\
            .join(Document.annotation)\
            .join(Document.revision)
        if before is not None:
            query = query.filter(Revision.date < before)
        query = query.order_by(Revision.date.asc())
        return query.all()

    # Submits a new original to this repository.
    #
    # * If the ID associated with this original is totally new, create a new
    #   document to hold it, then add the original as its first revision.
    #
    # * Otherwise, if the ID has been submitted before, add the document as a
    #   new revision if any property of the revision would differ.
    #
    # In either case, the revision model object used to store the original will
    # be returned.
    def submit(self, original, content_type, message_id, date, submitter):
        # This implementation contains an inherent data race. It determines
        # whether the document already exists by querying, then decides whether
        # to create or whether to append a revision based on what it finds
        # there.
        #
        # This is "safe" in the sense of not causing data corruption: if two
        # requests try to create the same document, one will win and the other
        # will receive an error without having any effect on the database. Both
        # transactions will attempt to insert a revision 1 of the same document,
        # violating a constraint. However, it's not terribly user-friendly when
        # this happens.
        document = self.ensure_document_by_id(message_id)
        revision = document.add_revision(original, content_type, date, submitter)
        return revision

    # In order to manipulate a document, first we have to ensure it exists. The
    # following logic implements a slightly race-prone way of determining this:
    # first we query for the document (knowing what ID it should have), and then
    # if we don't find one, we insert it.
    #
    # This is generally only meaningful in the context of operations that can
    # guarantee that the document will subsequently have a revision, as it
    # doesn't ensure that the returned document has one. If no revision is
    # attached at the end of the transaction, the transaction will fail to
    # commit. This can be surprising.
    def ensure_document_by_id(self, message_id):
        document = self.session.query(Document).filter_by(message_id = message_id).first()
        if document is None:
            document = Document(
                message_id       = message_id,
                current_revision = 0
            )
            self.session.add(document)

        return document

# ## MODELS
#
# The data model for a repository is as follows.

# A Revision holds an original, including its associated metadata.

from cadastre import sql
from .metadata import coalesce

from sqlalchemy import Column, Integer, String, DateTime, LargeBinary, JSON, ForeignKey, ForeignKeyConstraint
from sqlalchemy.orm import relationship, foreign

class Revision(sql.Base):
    __tablename__ = 'revision'
    # There's a foreign key back from message_id to document.message_id, but
    # it's hidden from SQLAlchemy to ensure that the relationships we actually
    # use in queries are automatically detected. The DB constraint is used for
    # schematic enforcement ("every revision has a document"), but not for app
    # logic.
    message_id   = Column(String, primary_key = True)
    revision     = Column(Integer, primary_key = True)
    date         = Column(DateTime(timezone = True), nullable = False)
    body         = Column(LargeBinary, nullable = False)
    content_type = Column(String, nullable = False)
    submitter    = Column(String,
        ForeignKey('user.email'),
    )

# An annotation holds structured data describing the document's effects on the
# game.

class Annotation(sql.Base):
    __tablename__ = 'annotation'
    message_id = Column(String, ForeignKey('document.message_id'), primary_key = True)
    revision   = Column(Integer, primary_key = True)
    changes    = Column(JSON, nullable = False)
    submitter  = Column(String,
        ForeignKey('user.email'),
    )

    document = relationship(lambda: Document,
        foreign_keys=[message_id],
        backref='annotations',
    )

# A Document groups revisions and identifies the current revision, as well as
# any current annotation.

class Document(sql.Base):
    __tablename__ = 'document'
    message_id       = Column(String, primary_key = True)
    current_revision = Column(Integer, nullable = False)
    current_annotation = Column(Integer)

    __table_args__ = (
        ForeignKeyConstraint(
            ['message_id', 'current_revision'],
            ['revision.message_id', 'revision.revision'],
        ),
        ForeignKeyConstraint(
            ['message_id', 'current_annotation'],
            ['annotation.message_id', 'annotation.revision'],
        ),
    )

    # Equal to the current revision as selected out of the database, if any.
    # This can and will be None for documents where no revision has been
    # recorded in the database.
    revision = relationship(Revision)

    # Equal to the current annotation as selected out of the database, if any.
    # This can and will be None for documents where no annotation has been
    # recorded in the database.
    #
    # The overlapping keys between this relationship and the `revision`
    # relationship cause an SQLAlchemy warning:
    #
    # SAWarning: relationship 'Document.annotation' will copy column
    # annotation.message_id to column document.message_id, which conflicts with
    # relationship(s): 'Document.revision' (copies revision.message_id to
    # document.message_id). Consider applying viewonly=True to read-only
    # relationships, or provide a primaryjoin condition marking writable columns
    # with the foreign() annotation.
    #
    # No permutation of foreign(), primaryjoin, and foreign_key options that
    # I've found has both suppressed this warning and produced correct queries.
    # However, given that this class' methods are the normal path to inserting
    # records into both relationships, and that those methods rely on
    # self.message_id to set the message_id of new rows in either related table,
    # the overlapping `message_id` is kept consistent, and I believe it is
    # impossible for the situations warned about to arise.
    #
    # I'd still like to know how to fix it though.
    annotation = relationship(Annotation, foreign_keys=[message_id, current_annotation])

    # The next revision of a document is, for simplicity's sake, the
    # next-highest number. This gives each document an easy, humane range of
    # revision numbers, generally starting with revision 1.
    #
    # This isn't perfect, and in fact should probably query the database rather
    # than computing it in memory, but it'll work as long as all inserts go
    # through this code. If the current revision of a document is ever _not_ the
    # highest revision, this will fail.
    @property
    def next_revision(self):
        # Assume the current revision is the absolute highest revision
        return self.current_revision + 1

    # The next revision number for annotations associated with this document is
    # also presumed to be one greater than the current annotation revision, or 1
    # if no annotation yet exists. This has the same race condition issues as
    # next_revision, and the same safety profile.
    @property
    def next_annotation(self):
        # Assume the current annotation is the absolute highest revision
        return coalesce(self.current_annotation, 0) + 1

    # Add a revision to this document, if the arguments would generate a
    # revision distinct from the most recent revision.
    def add_revision(self, body, content_type, date, submitter):
        if not self.currently_like(body, content_type, date):
            self.revision = Revision(
                message_id   = self.message_id,
                revision     = self.next_revision,
                date         = date,
                body         = body,
                content_type = content_type,
                submitter    = submitter,
            )
        return self.revision

    # A document is "currently like" a body, content type, message_id, and date
    # if it has a current revision, and the current revision's properties are
    # equal to those given as arguments.
    def currently_like(self, body, content_type, date):
        # Self is never currently like something if it has no current revision.
        if self.revision is None:
            return False

        # Check properties pairwise, returning as soon as we find one that
        # differs.
        if self.revision.body != body:
            return False
        if self.revision.content_type != content_type:
            return False
        if self.revision.date != date:
            return False

        # Found no differences. By exhaustion, self _is_ currently like the
        # arguments. Note that we do not consider the submitter to be part of
        # the "like" relationship, here: resubmitting the most recent revision
        # under another username isn't a meaningful change.
        return True

    # Set the annotations on this document. This will replace the current
    # annotations, if any, if the new annotation differs.
    def update_annotation(self, changes, submitter):
        if not self.currently_annotated_like(changes):
            self.annotation = stored_annotation = Annotation(
                message_id = self.message_id,
                revision   = self.next_annotation,
                changes    = changes,
                submitter  = submitter,
            )
        return self.annotation

    def currently_annotated_like(self, changes):
        # Not annotated, so can't be annotated like anything.
        if self.annotation is None:
            return False

        # Check properties pairwise, returning as soon as we find one that
        # differs.
        if self.annotation.changes != changes:
            return False

        # By exhaustion, self _is_ annotated like the arguments. Note that we do
        # not consider the submitter to be part of the "like" relationship,
        # here: resubmitting the most recent annotation under another username
        # isn't a meaningful change.
        return True


# ## COMPONENTS

# The repository for a request can be made available as a component, relying on
# the SQLAlchemy session component for the same request.

from apistar.backends.sqlalchemy_backend import Session

def request_repository(session: Session):
    return Repository(session)

from apistar import Component

components = [
    Component(Repository, init = request_repository, preload = False),
]
