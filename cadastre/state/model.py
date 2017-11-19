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
