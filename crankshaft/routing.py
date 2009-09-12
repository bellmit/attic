from functools import partial

def make_routable(endpoint):
    if not is_routable(endpoint):
        endpoint.__rule_args__ = []

def is_routable(endpoint):
    return hasattr(endpoint, '__rule_args__')

def to_rules(name, endpoint):
    print "%s has rules %r" % (name, endpoint.__rule_args__)
    for args, kwargs in endpoint.__rule_args__:
        yield args, dict(kwargs, endpoint=name)

def route(*args, **kwargs):
    def decorate_endpoint(endpoint):
        make_routable(endpoint)
        endpoint.__rule_args__.append((args, kwargs))
        return endpoint
    return decorate_endpoint

get = partial(route, methods=['GET'])
post = partial(route, methods=['POST'])
put = partial(route, methods=['PUT'])
delete = partial(route, methods=['DELETE'])
