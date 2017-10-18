# Annotations impose a formal interpretation on the documents stored in this
# registry. Using annotations, users can describe the effects of the document as
# applied to a model of the game, as well as attaching reporting summaries to
# those documents.
#
# Cadastre's annotation model is, at its core, nested dictionaries. Changes to
# that model are expressed in terms of sequences of structured changes,
# described throughout this package. The state of the game at a given point in
# time is the initial state of the game - an empty state - plus the sequential
# application of changes from each document whose date is no later than that
# point in time.
#
# Like documents, annotations are versioned. This allows abusive annotations to
# be detected and reversed, and allows annotations to evolve as user practices
# around modelling the game's state evolve.
#
# ## SERVICES

# Submits a new annotation for a document. This replaces the current annotation,
# if any.

from apistar import typesystem, reverse_url, Response
from apistar.interfaces import Auth
from http import HTTPStatus
from cadastre.authn import policy
from . import repository as repo

class Change(dict):
    pass

class Annotation(typesystem.Object):
    properties = {
        'changes': typesystem.array(items = Change),
    }

class Submission(typesystem.Object):
    properties = {
        'annotation_url': typesystem.string(format='URL'),
    }

@policy.authenticated
def submit_annotation(
    message_id,
    annotation: Annotation,
    repository: repo.Repository,
    auth: Auth,
) -> Submission:
    document = repository.get_document(message_id)
    annotation = document.update_annotation(annotation['changes'], auth.get_user_id())
    annotation_url = reverse_url(
        'retrieve_annotation',
        message_id=annotation.message_id,
        revision=annotation.revision,
    )
    return Response(
        Submission(annotation_url=annotation_url),
        # This should be SEE_OTHER, but apistar misrenders the response. See
        # <https://github.com/encode/apistar/issues/317>.
        status=HTTPStatus.OK,
        headers={
            'Location': annotation_url,
        },
    )

# Retrieve a specific annotation revision for a document.
from . import repository as repo

def retrieve_annotation(message_id, revision, repository: repo.Repository) -> Annotation:
    annotation = repository.retrieve_annotation(message_id, revision)
    return Annotation(changes=annotation.changes)

# List the  annotation revisions for a document.
from apistar import typesystem, reverse_url, Response
from http import HTTPStatus
from . import repository as repo

class Annotation(typesystem.Object):
    properties = {
        'revision': typesystem.Integer,
        'url': typesystem.string(format='URL'),
    }

class Annotations(typesystem.Object):
    properties = {
        'current': typesystem.string(format='URL'),
        'annotations': typesystem.array(items = Annotation),
    }

def document_annotations(message_id, repository: repo.Repository) -> Annotations:
    def annotation_url(annotation):
        return reverse_url(
            'retrieve_annotation',
            message_id=annotation.message_id,
            revision=annotation.revision,
        )
    document = repository.get_document(message_id)
    annotations = [
        Annotation(revision=annotation.revision, url=annotation_url(annotation))
        for annotation in document.annotations
    ]
    if document.annotation is None:
        return Annotations(annotations=annotations)
    return Annotations(annotations=annotations, current=current_url)

# ## WEB APPLICATION CONFIGURATION

# Annotations have a set of related API routes, which will all be mounted
# together. The root of this collection is the submission endpoint for creating
# and updating documents.

from apistar import Route

routes = [
    Route('/{message_id}/annotation', 'POST', submit_annotation),
    Route('/{message_id}/annotation', 'GET', document_annotations),
    Route('/{message_id}/annotation/{revision}', 'GET', retrieve_annotation),
]
