from itertools import chain
from werkzeug.routing import Map, Rule, RuleFactory
from werkzeug import Request, Response, ClosingIterator
from werkzeug.exceptions import HTTPException

class WebApplication(type):
    def __new__(meta, name, bases, dict):
        dict['__route_defs__'] = route_defs = meta.extract_route_defs(dict)
        routes = [Rule(*args, **kwargs) for args, kwargs in route_defs]
        inherited_route_defs = chain(*[
            base.__route_defs__ for base in bases if hasattr(base, '__route_defs__')
        ])
        inherited_routes = [Rule(*args, **kwargs) for args, kwargs in inherited_route_defs]
        dict['__route_map__'] = Map(routes + inherited_routes)

        def __call__(self, environ, start_response):
            request = Request(environ)
            adapter = self.__route_map__.bind_to_environ(environ)
            try:
                endpoint, values = adapter.match()
                handler = getattr(self, endpoint)
                response = handler(request, **values)
            except HTTPException, e:
                response = e
            return ClosingIterator(response(environ, start_response))

        dict['__call__'] = __call__

        return type.__new__(meta, name, bases, dict)

    @classmethod
    def extract_route_defs(meta, attributes):
        return [
            meta.route_def(name, method)
            for name, method in attributes.iteritems()
            if meta.is_routable(method)
        ]
    
    @classmethod
    def route_def(meta, name, method):
        return (
            method.__route_args__,
            dict(method.__route_kwargs__, endpoint=name)
        )
    
    @classmethod
    def is_routable(meta, method):
        return all((
            hasattr(method, '__route_args__'),
            hasattr(method, '__route_kwargs__')
        ))

def route(url):
    def decorate_method(method):
        method.__route_args__ = (url,)
        method.__route_kwargs__ = {}
        return method
    return decorate_method

class Example(object):
    __metaclass__ = WebApplication
    
    def __init__(self, global_config, message, **config):
        self.message = message
    
    @route('/')
    def index(self, request):
        return Response(self.message)

class InheritanceExample(Example):
    
    def index(self, request):
        return Response("I've totally hidden my parent class's behaviour.")
    
    @route('/ok')
    def ok(self, request):
        return Response('Ok.')
