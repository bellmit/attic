# ## USER INTERFACE
#
# Cadastre includes an HTML5 (React) interface to allow users to directly
# interact with the API. This is automatically made available (if built) every
# time Cadastre starts.
#
# Most of the heavy lifting for this is done by Webpack, so see
# `webpack.config.js` for pointers. Webpack places its output in `static` in
# this application; the following rules ensure that the files found there are
# mapped to predictable and unsurprising URLs.

# ## WEB APPLICATION CONFIGURATION

# The UI needs one extra route, to serve the `index.html` page at the `/` URL.

from apistar import Route, annotate
from apistar.renderers import HTMLRenderer

@annotate(renderers=[HTMLRenderer()], exclude_from_schema=True)
def ui():
    # This could be cached on first rendering, and in production that'd be a
    # somewhat smart thing to do, but during development, the page content
    # changes fairly regularly. Refresh-driven development is a nice way to
    # build HTML5 apps, so reload on each pageview just in case the file on disk
    # has changed.
    with open('static/index.html', 'r') as index:
        return index.read()

routes = [
    Route('/', 'GET', ui),
]
