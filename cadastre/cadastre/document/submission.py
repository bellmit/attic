# API support for submitting documents to and retrieving documents from the
# registry.

# ## SERVICES

# This service accepts new originals.
#
# The original's ID can, optionally, be provided using the `Message-ID` header
# of the request. The original's publication date can be provided using the
# `Date` header of the request. If either is not provided, this service will
# attempt to detect it from the message body. This detection supports
# `message/rfc822` messages only, and relies on the corresponding headers having
# meaningful and parseable values in the MIME message. A message whose ID or
# publication date cannot be determined will be rejected with a 400 error.

from apistar import http, typesystem, Response, reverse_url
from apistar.interfaces import Auth, Router
from http import HTTPStatus
from . import metadata as meta
from . import repository as repo
from cadastre.authn import policy

class Submission(typesystem.Object):
    properties = {
        'message_id': typesystem.string(),
        'download_url': typesystem.string(format='URL'),
        'annotation_url': typesystem.string(format='URL'),
    }

@policy.authenticated
def submit_original(
    original: http.Body,
    content_type: http.Header,
    metadata: meta.MergedMetadata,
    repository: repo.Repository,
    auth: Auth,
) -> Submission:
    revision = repository.submit(
        original,
        content_type,
        metadata.message_id,
        metadata.date,
        metadata.subject,
        auth.get_user_id(),
    )
    download_url = reverse_url(
        'retrieve_revision',
        message_id=revision.message_id,
        revision=revision.revision,
    )
    annotation_url = reverse_url(
        'submit_annotation',
        message_id=revision.message_id,
    )
    return Response(
        Submission(
            message_id=revision.message_id,
            download_url=download_url,
            annotation_url=annotation_url,
        ),
        # This should be SEE_OTHER, but apistar misrenders the response. See
        # <https://github.com/encode/apistar/issues/317>.
        status=HTTPStatus.OK,
        headers={
            'Location': download_url,
        },
    )

# This service retrieves stored revisions, verbatim, and returns them with the
# same content type used to submit them. It accepts two parameters
# (conventionally, by path): the message ID of the document, and the revision
# number.

from apistar import Response, annotate
from . import repository as repo

def retrieve_revision(message_id, revision, repository: repo.Repository):
    revision = repository.retrieve_revision(message_id, revision)
    return Response(revision.body, content_type = revision.content_type)

# This service enumerates stored documents matching specific criteria. By
# default, it returns a list of all known documents.
#
# Supported criteria:
#
# * `annotated`: if not set, include all documents. If set to `true`, include
#   only documents with annotations. If set to an empty string, include only
#   documents without annotations. A future version will likely use `false` in
#   place of the empty string, pending the resolution of
#   <https://github.com/encode/apistar/issues/354>. A message with an empty
#   annotation is, for the purposes of this criterion, annotated.

from . import repository as repo

# Filter generator that implements the `annotated` criterion: if `annotated` is
# set, return the appropriate filter list; otherwise, return an empty list.
def annotation_filters(annotated):
    if annotated is not None:
        if annotated:
            yield repo.is_annotated
        else:
            yield repo.not_annotated

from apistar import typesystem, reverse_url

# The following document models define the public-facing UI for document
# objects. We use these for both lists of documents and for metadata about
# single documents, to simplify client development.
class Revision(typesystem.Object):
    properties = {
        'revision': typesystem.integer(),
        'date': typesystem.string(format='ISO8601'),
        'subject': typesystem.string(),
        'download_url': typesystem.string(format='URL'),
    }

class Annotation(typesystem.Object):
    properties = {
        'revision': typesystem.integer(),
        'download_url': typesystem.string(format='URL'),
    }

class Document(typesystem.Object):
    properties = {
        'message_id': typesystem.string(),
        'revisions': typesystem.array(items = Revision),
        'annotations': typesystem.array(items = Annotation),
        'current_revision': Revision,
        'current_annotation': Annotation,
        'annotate_url': typesystem.string(format='URL'),
    }

# Construct a wire object from various repository objects. The internal
# representations use rich python objects; the external, only types that fit in
# a JSON document.

def model_to_revision(model):
    return Revision(
            revision=model.revision,
            date=model.date,
            subject=model.subject,
            download_url=reverse_url(
                'retrieve_revision',
                message_id=model.message_id,
                revision=model.revision,
            )
        )

def model_to_annotation(model):
    return Annotation(
        revision=model.revision,
        download_url=reverse_url(
            'retrieve_annotation',
            message_id=model.message_id,
            revision=model.revision,
        )
    )

def model_to_document(document):
    current_annotation = dict()
    if document.annotation is not None:
        current_annotation = dict(current_annotation=model_to_annotation(document.annotation))

    return Document(
        message_id=document.message_id,
        annotate_url=reverse_url('submit_annotation', message_id=document.message_id),
        revisions=[model_to_revision(r) for r in document.revisions],
        annotations=[model_to_annotation(a) for a in document.annotations],
        current_revision=model_to_revision(document.revision),
        **current_annotation,
    )

def list_documents(repository: repo.Repository, annotated: bool = None):
    document_filters = annotation_filters(annotated)
    documents = repository.get_documents(document_filters)
    return [model_to_document(m) for m in documents]

# Service to retrieve metadata about a single document. This is more complete
# than the information returned in the document list.

from . import repository as repo

def retrieve_document(message_id, repository: repo.Repository):
    document = repository.get_document(message_id)
    return model_to_document(document)

# ## WEB APPLICATION CONFIGURATION

# Documents have a set of related API routes, which will all be mounted
# together. The root of this collection is the submission endpoint for creating
# and updating documents.

from apistar import Route

routes = [
    Route('', 'POST', submit_original),
    Route('', 'GET', list_documents),
    Route('/{message_id}', 'GET', retrieve_document),
    Route('/{message_id}/{revision}/original', 'GET', retrieve_revision),
]
