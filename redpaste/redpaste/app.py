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

class UrlLookup(object):
    def __init__(self, adapter):
        self.adapter = adapter
    
    def __call__(
        self,
        endpoint,
        method=None,
        force_external=False,
        **kwargs
    ):
        return self.adapter.build(
            endpoint,
            values=kwargs,
            method=method,
            force_external=force_external
        )

def template(name):
    """Returns a decorator that replaces the return value of a function.
    The decorated function is expected to return a dict, which is converted
    into keyword arguments for Mako. The resulting, rendered template is
    wrapped up in a Response."""
    def template_decorator(f):
        @wraps(f)
        def template_responder(self, request, *args, **kwargs):
            response = f(self, request, *args, **kwargs)
            template = template_lookup.get_template(name)
            body = template.render(**dict(
                url_for=UrlLookup(request.url_adapter),
                **response
            ))
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
        return redirect(
            request.url_adapter.build(
                'paste',
                values=dict(id=paste.id),
                method='GET'
            ),
            code=303
        )
    
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