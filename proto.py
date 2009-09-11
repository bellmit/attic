from werkzeug.routing import Map, Rule
from werkzeug import Request, Response, ClosingIterator
from werkzeug.exceptions import HTTPException

class WebApplication(type):
    def __new__(meta, name, bases, dict):
        routes = [
            Rule(
                *method.__route_args__,
                endpoint=name,
                **method.__route_kwargs__
            ) for name, method in dict.iteritems() if hasattr(method, '__route_args__')
        ]
        # TODO: inherited routes?
        dict['__route_map__'] = Map(routes)

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
