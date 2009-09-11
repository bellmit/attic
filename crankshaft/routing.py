from functools import partial

def is_routable(function):
    return all((
        callable(function),
        hasattr(function, '__rule_args__'),
        hasattr(function, '__rule_kwargs__')
    ))

def to_rule(name, function):
    return (
        function.__rule_args__,
        dict(function.__rule_kwargs__, endpoint=name)
    )

def route(*args, **kwargs):
    def decorate_method(method):
        method.__rule_args__ = args
        method.__rule_kwargs__ = kwargs
        return method
    return decorate_method

get = partial(route, methods=['GET'])
post = partial(route, methods=['POST'])
put = partial(route, methods=['PUT'])
delete = partial(route, methods=['DELETE'])
