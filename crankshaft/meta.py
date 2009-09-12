from itertools import chain
from werkzeug.routing import Map, Rule, RuleFactory
from werkzeug import Request, ClosingIterator
from werkzeug.exceptions import HTTPException
from crankshaft.routing import is_routable, to_rules

# Define a real, honest-to-Zod function named __call__ so that
# webapp-derived types look as normal as possible.
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

class WebApplication(type):
    def __new__(meta, name, bases, dict):
        dict['__rule_defs__'] = rule_defs = list(meta.extract_rule_defs(dict))
        rules = [Rule(*args, **kwargs) for args, kwargs in rule_defs]
        
        inherited_rule_defs = chain(*[
            base.__rule_defs__ for base in bases if hasattr(base, '__rule_defs__')
        ])
        inherited_rules = [Rule(*args, **kwargs) for args, kwargs in inherited_rule_defs]
        dict['__rule_map__'] = Map(rules + inherited_rules)
        dict['__call__'] = __call__
        
        return type.__new__(meta, name, bases, dict)

    @classmethod
    def extract_rule_defs(meta, attributes):
        for name, method in attributes.iteritems():
            if meta.is_routable(method):
                for rule in meta.to_rules(name, method):
                    yield rule
    
    @classmethod
    def to_rules(meta, name, method):
        return to_rules(name, method)
    
    @classmethod
    def is_routable(meta, method):
        return is_routable(method)
