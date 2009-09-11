from functools import partial

def is_routable(endpoint):
    return all((
        hasattr(endpoint, '__rule_args__'),
        hasattr(endpoint, '__rule_kwargs__')
    ))

def to_rule(name, endpoint):
    return (
        endpoint.__rule_args__,
        dict(endpoint.__rule_kwargs__, endpoint=name)
    )

def route(*args, **kwargs):
    def decorate_endpoint(endpoint):
        endpoint.__rule_args__ = args
        endpoint.__rule_kwargs__ = kwargs
        return endpoint
    return decorate_endpoint

get = partial(route, methods=['GET'])
post = partial(route, methods=['POST'])
put = partial(route, methods=['PUT'])
delete = partial(route, methods=['DELETE'])
