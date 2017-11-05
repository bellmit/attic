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
# * events_from (datetime, ISO8601): include event descriptions for documents
#   whose dates are not less than this date.
#
# * office (string): include event descriptions only for events in this office.
#
import functools
from dateutil import parser
from cadastre.document import state
from cadastre.document import repository as repo

def compute_state(repository: repo.Repository, before = None, events_from = None, office = None):
    # There's no support for datetime query params in apistar yet, but see
    # <https://github.com/encode/apistar/issues/353>
    if before is not None:
        before = parser.parse(before)
    if events_from is not None:
        events_from = parser.parse(events_from)

    annotations = repository.get_annotations(
        before=before,
    )

    # This whole method needs some serious refactoring. I've left it like this
    # as I expect to reimplement the whole thing - this is, effectively, a first
    # draft. A future implementation is likely to use a more sophisticated
    # evaluation model that takes all of this labelling into account.

    # First, compute the state. This includes all events going back to the year
    # dot; we don't filter annotations for time at this point.
    patches = (state.Patch(annotation.changes) for annotation in annotations)
    initial_state = state.initial_state()

    def apply_patch(state_errors, patch):
            state, errors = state_errors
            next_state, next_errors = patch.apply(state)
            return next_state, errors + next_errors

    computed_state, computed_errors = functools.reduce(apply_patch, patches, (initial_state, []))

    # Second, compute the event list. We need to draw information from the
    # associated document to decorate events, so we can't just do this on
    # ingestion or require the annotator do it. We prune the annotation list
    # based on ?events_from and ?office, as above.
    if events_from is None:
        def check_annotation(annotation):
            return True
    else:
        def check_annotation(annotation):
            return annotation.document.revision.date >= events_from
    if office is None:
        def check_office(event):
            return True
    else:
        def check_office(event):
            return event['office'] == office
    def timestamp_event(annotation, event):
        return dict(timestamp=annotation.document.revision.date.isoformat(), **event)
    def timestamped_events(annotation):
        return [timestamp_event(annotation, e) for e in annotation.events if check_office(e)]

    events = sum((timestamped_events(a) for a in annotations if check_annotation(a)), [])

    # Finally, stick it all together into a response.

    return dict(events=events, state=computed_state, errors=computed_errors)

# ## WEB APPLICATION CONFIGURATION

# Annotations have a set of related API routes, which will all be mounted
# together. The root of this collection is the state computation endpoint.

from apistar import Route

routes = [
    Route('', 'GET', compute_state),
]
