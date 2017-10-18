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
#
# ## WEB APPLICATION CONFIGURATION

# Documents have a set of related API routes, which will all be mounted
# together. The root of this collection is the submission endpoint for creating
# and updating documents.

from apistar import Route
from . import submission
from . import annotation

routes = [
    *submission.routes,
    *annotation.routes,
]

# Documents include some helper components which provide data about requests.
# These components must be available in the application for the routes defined
# in this chapter to function correctly.

from . import metadata as meta
from . import repository as repo

components = [
    *meta.components,
    *repo.components,
]
