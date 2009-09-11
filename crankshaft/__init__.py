from functools import partial
import crankshaft.meta

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

class WebApplication(object):
    __metaclass__ = crankshaft.meta.WebApplication
