from itertools import chain
from werkzeug.routing import Map, Rule, RuleFactory
from werkzeug import Request, Response, ClosingIterator
from werkzeug.exceptions import HTTPException

class BadlyNamedMetaclass(type):
    def __new__(meta, name, bases, dict):
        dict['__rule_defs__'] = rule_defs = meta.extract_rule_defs(dict)
        rules = [Rule(*args, **kwargs) for args, kwargs in rule_defs]
        
        inherited_rule_defs = chain(*[
            base.__rule_defs__ for base in bases if hasattr(base, '__rule_defs__')
        ])
        inherited_rules = [Rule(*args, **kwargs) for args, kwargs in inherited_rule_defs]
        dict['__rule_map__'] = Map(rules + inherited_rules)
        
        # Define a real, honest-to-Zod function named __call__ so that
        # the resulting type looks as normal as possible.
        def __call__(self, environ, start_response):
            request = Request(environ)
            adapter = self.__rule_map__.bind_to_environ(environ)
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
    def extract_rule_defs(meta, attributes):
        return [
            meta.rule_def(name, method)
            for name, method in attributes.iteritems()
            if meta.is_routable(method)
        ]
    
    @classmethod
    def rule_def(meta, name, method):
        return (
            method.__rule_args__,
            dict(method.__rule_kwargs__, endpoint=name)
        )
    
    @classmethod
    def is_routable(meta, method):
        return all((
            hasattr(method, '__rule_args__'),
            hasattr(method, '__rule_kwargs__')
        ))

class WebApplication(object):
    __metaclass__ = BadlyNamedMetaclass

def route(*args, **kwargs):
    def decorate_method(method):
        method.__rule_args__ = args
        method.__rule_kwargs__ = kwargs
        return method
    return decorate_method

class Example(WebApplication):
    def __init__(self, global_config, message, **config):
        self.message = message
    
    @route('/')
    def index(self, request):
        return Response(self.message)

class InheritanceExample(Example):
    
    @route('/', methods=['POST'])
    def post_index(self, request):
        return Response("I've totally hidden my parent class's behaviour.")
    
    @route('/ok')
    def ok(self, request):
        return Response('Ok.')
