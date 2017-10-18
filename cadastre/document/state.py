# The state of the game at any given point is given by an initial state (see
# below) plus the cumulative effect of a sequence of changes, represented by
# Patch objects.
#
# The system elaborated on below closely mirrors the structure of the JSON-Patch
# RFC, but the specific operations provided are designed to meet needs unique to
# Cadastre. The operations are documented below.

## STATES
#
# The initial state is simply an empty dictionary. The final state is,
# effectively, any dictionary that can represent a JSON structure - that is, any
# dict whose values are each ints, strings, floats, booleans, None, or lists or
# objects whose values are all of these kinds (recursively).

def initial_state():
    return dict()

## PATCHES
#
# A patch represents a total change to a state. Each patch encodes a sequence of
# operations, which are applied in order to produce the final state for the
# patch given a specific initial state.

import functools

class Patch(object):
    # Construct a patch from a list of dicts describing single operations. The
    # individual elements of the operations list will be converted into
    # Operation objects by `parse_operations`, below.
    def __init__(self, operations):
        self.operations = parse_operations(operations)

    # Applying a Patch to a state yields a new state (plus a list of warnings)
    # computed by applying the patch's operations to the initial state.
    def apply(self, state):
        def apply_op(state_errors, op):
            state, errors = state_errors
            next_state, next_error = op.apply(state)
            if next_error is not None:
                errors = errors + [next_error]
            return next_state, errors

        return functools.reduce(apply_op, self.operations, (state, []))

## OPERATIONS
#
# An operation is a primitive state change. (In fact, each Operation is a
# perfectly valid Patch. They share an API.)

# op: create
# path: <json-pointer>
#
# Ensures that a state subtree exists, creating it if necessary. This operation
# walks the nodes of the `path`. For each node, if the corresponding value is
# absent, it is created as an empty dict. If the value is present and not a
# dict, this operation fails.

from jsonpointer import JsonPointer
import copy

class CreateOperation(object):
    def __init__(self, path, **kwargs):
        self.pointer = JsonPointer(path)

    def apply(self, state):
        step = result = copy.deepcopy(state)
        for part in self.pointer.parts:
            try:
                if part not in step:
                    step[part] = initial_state()
                step = self.pointer.walk(step, part)
            except TypeError:
                return state, f"Node on {self.pointer.path} (before {part}) exists and is not indexable"
        return result, None

# op: set
# path: <json-pointer>
# value: <any>
#
# Unconditionally sets a value. Fails if the target is not an appropriate
# container.

from jsonpointer import JsonPointer, JsonPointerException

class SetOperation(object):
    def __init__(self, path, value, **kwargs):
        self.pointer = JsonPointer(path)
        self.value = value

    def apply(self, state):
        try:
            return self.pointer.set(state, self.value, inplace=False), None
        except JsonPointerException as e:
            return state, str(e)

# op: error
# message: <string>
#
# Always fails and produces `message` as a warning.

class ErrorOperation(object):
    def __init__(self, message, **kwargs):
        self.message = message

    def apply(self, state):
        return state, self.message

# Parses a dict, which describes a single Operation, into that Operation.
operations = {
    'create': CreateOperation,
    'error': ErrorOperation,
    'set': SetOperation,
}

def parse_operation(operation):
    try:
        op = operation['op']
        return operations[op](**operation)
    except Exception as e:
        return ErrorOperation(str(e))

# Parses a sequence of dicts, where each dict describes an Operation, into a
# sequence of Operations.
def parse_operations(operations):
    return [parse_operation(op) for op in operations]

