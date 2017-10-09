# A repository handles submission and retrieval of documents against an
# underlying data store. A `Repository` wraps a specific SQLAlchemy Session, and
# should not outlive it.

class Repository(object):
    def __init__(self, session):
        self.session = session

    # Retrieves a previously-submitted revision from this repository. This is a
    # very straightforward SELECT by primary key, which either returns one row
    # or zero rows.
    def retrieve_revision(self, message_id, revision):
        return self.session.query(Revision).filter_by(message_id = message_id, revision = revision).first()

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

from sqlalchemy import Column, Integer, String, DateTime, LargeBinary, ForeignKey, ForeignKeyConstraint
from sqlalchemy.orm import relationship

class Revision(sql.Base):
    __tablename__ = 'revision'
    message_id   = Column(String,
        ForeignKey('document.message_id', deferrable = True, initially = 'DEFERRED'),
        primary_key = True)
    revision     = Column(Integer, primary_key = True)
    date         = Column(DateTime(timezone = True), nullable = False)
    body         = Column(LargeBinary, nullable = False)
    content_type = Column(String, nullable = False)
    submitter    = Column(String,
        ForeignKey('user.email'),
    )

# A Document groups revisions and identifies the current revision.

class Document(sql.Base):
    __tablename__ = 'document'
    message_id       = Column(String, primary_key = True)
    current_revision = Column(Integer, nullable = False)

    __table_args__ = (
        ForeignKeyConstraint(
            ['message_id', 'current_revision'],
            ['revision.message_id', 'revision.revision'],
            deferrable = True,
            initially = 'DEFERRED',
        ),
    )

    # Equal to the current revision as selected out of the database, if any.
    # This can and will be None for documents where no revision has been
    # recorded in the database.
    current = relationship(Revision, foreign_keys = [message_id, current_revision])

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

    # Add a revision to this document, if the arguments would generate a
    # revision distinct from the most recent revision.
    def add_revision(self, body, content_type, date, submitter):
        if not self.currently_like(body, content_type, date):
            self.current = Revision(
                message_id   = self.message_id,
                revision     = self.next_revision,
                date         = date,
                body         = body,
                content_type = content_type,
                submitter    = submitter,
            )
        return self.current

    # A document is "currently like" a body, content type, message_id, and date
    # if it has a current revision, and the current revision's properties are
    # equal to those given as arguments.
    def currently_like(self, body, content_type, date):
        # Self is never currently like something if it has no current revision.
        if self.current is None:
            return False

        # Check properties pairwise, returning as soon as we find one that
        # differs.
        if self.current.body != body:
            return False
        if self.current.content_type != content_type:
            return False
        if self.current.date != date:
            return False

        # Found no differences. By exhaustion, self _is_ currently like the
        # arguments.
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
