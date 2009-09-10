from werkzeug.routing import Map, Rule
from werkzeug import Request, Response, ClosingIterator
from werkzeug.exceptions import HTTPException

class Example(object):
    def __init__(self, global_config, message, **config):
        self.message = message
    
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
    
    __route_map__ = Map([
        Rule('/', endpoint='index')
    ])
    
    def index(self, request):
        return Response(self.message)
