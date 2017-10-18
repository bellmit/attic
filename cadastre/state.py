# Endpoints for evaluating the current state based on stored annotations.
#
# This largely builds on the concepts described in the `documents` module, and I
# recommend reading that module first.
#
# ## ENDPOINTS

# Calculates the game state as of some point in time (by default, considering
# all messages).
#
# This endpoint accepts the following optional query parameters:
#
# * before (datetime, ISO-8601): if this is included, then only documents dated
#   strictly before this moment in time will be considered when computing the
#   state of the game. This allows retroactive computation of the state as of a
#   specific point in time.
#
import functools
from dateutil import parser
from cadastre.document import state
from cadastre.document import repository as repo

def compute_state(repository: repo.Repository, before = None):
    # There's no support for datetime query params in apistar yet, but see
    # <https://github.com/encode/apistar/issues/353>
    if before is not None:
        before = parser.parse(before)

    annotations = repository.get_annotations(
        before=before,
    )
    patches = (state.Patch(annotation.changes) for annotation in annotations)
    initial_state = state.initial_state()

    def apply_patch(state_errors, patch):
            state, errors = state_errors
            next_state, next_errors = patch.apply(state)
            return next_state, errors + next_errors

    computed_state, computed_errors = functools.reduce(apply_patch, patches, (initial_state, []))

    return dict(state=computed_state, errors=computed_errors)

# ## WEB APPLICATION CONFIGURATION

# Annotations have a set of related API routes, which will all be mounted
# together. The root of this collection is the state computation endpoint.

from apistar import Route

routes = [
    Route('', 'GET', compute_state),
]
