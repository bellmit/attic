# Documents are the core of the registry service. Every recorded fact about
# Agora's game state is derived from some information associated with a specific
# document. The following services allow clients to
#
# * Submit a new document,
# * Submit a revision of an existing document, and
# * Retrieve any revision of any existing document.
#
# ## TERMINOLOGY
#
# An original is any stream of bytes. A original additionally has an ID, a
# publication date.
#
# A document is a list of one or more originals. The originals in the list are
# the revisions of the document. New revisions are added to the end of a
# document, and the last revision is the "current" revision of the document.

# ## THE NULL RENDERER
#
# Documents are stored in exactly their native form - `bytes` objects - under
# the hood, and do not need "rendering." We use the null renderer to prevent API
# Star from trying to convert to JSON or doing content negotiation with clients.

from apistar.renderers import Renderer

class NullRenderer(Renderer):
    # Do nothing to the data - hence the name "null renderer."
    def render(self, data):
        return data

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
        'download_url': typesystem.string(format='URL'),
    }

@policy.authenticated
def submit_original(
    original: http.Body,
    content_type: http.Header,
    metadata: meta.MergedMetadata,
    repository: repo.Repository,
    auth: Auth,
) -> Submission:
    revision = repository.submit(original, content_type, metadata.message_id, metadata.date, auth.get_user_id())
    download_url = reverse_url(
        'retrieve_revision',
        message_id=revision.message_id,
        revision=revision.revision,
    )
    return Response(
        Submission(download_url=download_url),
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
from apistar.exceptions import NotFound
from . import repository as repo

@annotate(renderers = [NullRenderer()])
def retrieve_revision(message_id, revision, repository: repo.Repository):
    revision = repository.retrieve_revision(message_id, revision)
    if revision is None:
        raise NotFound()
    return Response(revision.body, content_type = revision.content_type)

# ## WEB APPLICATION CONFIGURATION

# Documents have a set of related API routes, which will all be mounted
# together. The root of this collection is the submission endpoint for creating
# and updating documents.

from apistar import Route

routes = [
    Route('/', 'POST', submit_original),
    Route('/{message_id}/{revision}/original', 'GET', retrieve_revision),
]

# Documents include some helper components which provide data about requests.
# These components must be available in the application for the routes defined
# in this chapter to function correctly.

from . import metadata as meta
from . import repository as repo

components = meta.components + repo.components
