# This service provides a JSON-over-HTTP interface to a deed registry service.
# This service records documents - generally email messages - for later perusal.
# Eventually, it will also support annotating those documents with a formal
# language describing state changes, allowing this service to model subsets of
# Agora's game state.
#
# This service embeds the assumption that all relevant changes to Agora's state
# can, in some way, be ascribed to a specific document.
#
# ## DEPENDENCIES
#
# This program relies on a number of libraries, listed in the `requirements.txt`
# file in the root of the source tree. It also relies on a Python runtime
# environment, running at least Python 3.6.
#
# This service stores data in a PostgreSQL database. Information on provisioning
# this database will be provided later in this document, on the section on
# configuration.
#
# ## INSTALLATION
#
# To install this service, fetch and unpack the source code (this archive), then
# run `pip install -r requirements.txt`.
#
# ## STARTUP
#
# This program should be started using the `bin/web` script in the root of this
# source tree. It can also be run with gunicorn directly: see that script for
# recommended options.
#
# ## STRUCTURE
#
# This service is built on top of the API Star
# <https://github.com/encode/apistar> framework, and makes extensive use of the
# services provided by that framework.
#
# The following is broken up into chapters, describing discrete pieces of the
# service's functionality (and implementing them as we go along).

# ## GREETINGS
#
# The simplest possible service. This service returns either a JSON document
# containing a generic greeting (if no `name` parameter is provided) or a JSON
# document with a personalized greeting (if a `name` parameter is provided).
# Conventionally, the `name` parameter is a URL path parameter or a query
# parameter, and this service is exposed as a `GET` interface, but it also
# supports configuration as a `POST` endpoint accepting either a form parameter
# named "name" or a single JSON string.

def welcome(name=None):
    if name is None:
        return {'message': 'Welcome to API Star!'}
    return {'message': 'Welcome to API Star, %s!' % name}

# ## THE WEB APPLICATION

# The preceding services must be bound together into a URL hierarchy. We assign
# a URL to each service, as well as to API Star's generated documentation for
# our service (useful for developers writing clients). The generated
# documentation relies on static files for style sheets, so we assign a URL for
# that, as well.

from apistar import Include, Route
from apistar.handlers import docs_urls, static_urls

routes = [
    Route('/', 'GET', welcome),
    Include('/docs', docs_urls),
    Include('/static', static_urls),
]

# Finally, expose all of the endpoints registered above as an API Star
# application. This can be bound to a WSGI framework and executed as a web
# application - as gunicorn does.

from apistar.frameworks.wsgi import WSGIApp as App

app = App(routes=routes)
