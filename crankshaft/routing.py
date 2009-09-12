from functools import partial

def make_routable(target):
    if not is_routable(target):
        target.__rule_args__ = []

def is_routable(target):
    return hasattr(target, '__rule_args__')

def routes(target, *extra_args, **extra_kwargs):
    if is_routable(target):
        for args, kwargs in target.__rule_args__:
            yield (args + extra_args), dict(kwargs, **extra_kwargs)

def route(*args, **kwargs):
    def decorate_target(target):
        make_routable(target)
        target.__rule_args__.append((args, kwargs))
        return target
    return decorate_target

get = partial(route, methods=['GET'])
post = partial(route, methods=['POST'])
put = partial(route, methods=['PUT'])
delete = partial(route, methods=['DELETE'])
