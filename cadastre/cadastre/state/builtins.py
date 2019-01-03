# ## BUILTINS
#
# Extra primitives needed to manipulate states in Actinide. This library uses
# the following translations of JSON concepts:
#
# * A JSON object is represented as a novel Actinide primitive, an "object".
#   These are Python dicts.
#
# * A JSON array is represented as an Actinide vector.
#
# * The JSON null literal is represented as an Actinide nil.
#
# * JSON integers are represented as Actinide integers.
#
# * JSON reals are represented as Actinide decimals.
#
# * JSON strings are represented as Actinide strings.
#
# * JSON booleans are represented as Actinide booleans.
#
# This registry contains these hooks, allowing the module to be loaded into
# Actinide:

from actinide.builtin import Registry
An = Registry()

# ### LISTS
#
# Mostly provided by Actinide. However, we need one additional builtin:

from actinide import types as t

@An.fn
def list_contains(list, elem):
    while t.cons_p(list) and not t.nip_p(list):
        head, list = t.uncons(list)
        if head == elem:
            return True
    return False

# ### OBJECTS

from actinide import types as t

@An.fn
def object():
    return dict()

@An.fn
def object_p(value):
    return isinstance(value, dict)

@An.fn
def object_keys(obj):
    return t.list(*obj.keys())

@An.fn
def object_has_key_p(obj, key):
    return key in obj

@An.fn
def object_get(obj, key):
    return obj[key]

@An.fn
def object_set(obj, key, value):
    obj[key] = value
    return obj

@An.fn
def object_remove(obj, key):
    del obj[key]
    return obj

# ### STATE ACCESS

import builtins as b
import jsonpointer as jp

class State(b.object):
    def __init__(self, state):
        self.An = Registry()
        self.state = state

        self.An.fn(self.get)
        self.An.fn(self.set)
        self.An.fn(self.delete)

    def get(self, path):
        return jp.resolve_pointer(self.state, path)

    def set(self, path, value):
        return jp.set_pointer(self.state, path, value)

    def delete(self, path):
        pointer = jp.JsonPointer(path)
        node, key = pointer.to_last(self.state)
        del node[key]
        return self.state

# ### METADATA CAPTURE FUNCTIONS

class Events(b.object):
    def __init__(self):
        self.An = Registry()
        self.events = []

        self.An.fn(self.event)

    def event(self, office, message):
        self.events.append(dict(
            office=office,
            message=message,
        ))

class Warnings(b.object):
    def __init__(self):
        self.An = Registry()
        self.warnings = []

        self.An.fn(self.warn)

    def warn(self, message):
        self.warnings.append(dict(
            message=message,
        ))

# ### Import code from a key

An.eval('''
(begin
    (define (state-export path form)
        (set path (display form)))

    (define (state-import path)
       (read (string-to-input-port (get path))))

    (define-macro (export path . forms)
        (state-export path `(begin ,@forms))
        (state-import path))

    (define-macro (import path)
        (state-import path)))
''')
