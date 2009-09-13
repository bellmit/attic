from werkzeug import Response
from redfox import WebApplication, get, post
from redpaste.storage import PasteStore
from functools import wraps
from pkg_resources import resource_filename
from mako.template import Template
from mako.lookup import TemplateLookup
from werkzeug import redirect
from werkzeug.exceptions import NotFound
from redpaste.highlight import css_stylesheet
from pygments.util import ClassNotFound
from werkzeug import SharedDataMiddleware

# Mako uses TemplateLookup objects to handle references between
# templates.
template_lookup = TemplateLookup(directories=[
    # Look in our distribution, in the ``redpaste/templates/`` directory.
    resource_filename(__name__, 'templates')
], module_directory='/tmp/mako_modules')

# Any requests that get routed to shared_data that don't match a
# file in the appropriate package data directory will lead straight
# to a 404 Not Found response.
shared_data = SharedDataMiddleware(NotFound(), {
    '/styles': ('redpaste', 'styles')
})

def template(name):
    """Returns a decorator that replaces the return value of a function.
    The decorated function is expected to return a dict, which is converted
    into keyword arguments for Mako. The resulting, rendered template is
    wrapped up in a Response."""
    def template_decorator(f):
        @wraps(f)
        def template_responder(*args, **kwargs):
            response = f(*args, **kwargs)
            template = template_lookup.get_template(name)
            print "Rendering %s with %r" % (name, response)
            body = template.render(**response)
            return Response(body, mimetype='text/html')
        return template_responder
    return template_decorator


class Pastebin(WebApplication):
    def __init__(self, global_config, database_url, **local_config):
        # Paster app_factory entry point. The configuration entries from
        # the application config file will be passed in via local_config,
        # keyword parameters, and global_config.
        self.paste_store = PasteStore(database_url)

    @get('/')
    @template('index.mako')
    def index(self, request):
        return dict(
            latest=self.paste_store.latest()
        )
    
    @post('/')
    def new_paste(self, request):
        author = request.form['author']
        body = request.form['body']
        syntax = request.form['syntax']
        
        paste = self.paste_store.new(author, body, syntax)
        return redirect('/%s' % paste.id, code=303)
    
    @get('/<id>')
    @template('paste.mako')
    def paste(self, request, id):
        try:
            return dict(
                latest=self.paste_store.latest(),
                paste=self.paste_store.get(id)
            )
        except KeyError:
            raise NotFound
    
    @get('/styles/syntax/<name>')
    def syntax_styles(self, request, name):
        try:
            return Response(
                css_stylesheet(name),
                mimetype='text/css'
            )
        except ClassNotFound:
            raise NotFound
    
    @get('/styles/<path>')
    def static_content(self, request, path):
        return shared_data