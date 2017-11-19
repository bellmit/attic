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
from actinide import ports
from actinide import reader as ar
from apistar import reverse_url
from dateutil import parser

from cadastre.document import repository as repo
from cadastre.state import model
from cadastre.state.session import Session

def compute_state(repository: repo.Repository, before = None):
    # There's no support for datetime query params in apistar yet, but see
    # <https://github.com/encode/apistar/issues/353>
    if before is not None:
        before = parser.parse(before)

    annotations = repository.get_annotations(
        before=before,
    )

    def annotate_dict(annotation, event):
        return dict(
            timestamp=annotation.document.revision.date.isoformat(),
            message_id=annotation.document.message_id,
            annotation_url=reverse_url(
                'retrieve_annotation',
                message_id=annotation.message_id,
                revision=annotation.revision,
            ),
            **event,
        )


    # Set up some initial state.
    state = model.initial_state()
    warnings = []
    events = []

    # Run each annotation against that state, in a session per annotation.
    for annotation in annotations:
        try:
            session = Session(state)
            port = ports.string_to_input_port(annotation.program)

            # Simulate a top-level `(begin ...)` form around the whole program
            # to simplify writing these. This way authors don't need to stick to
            # strict one-form programs (zero-form programs and n-form programs
            # are both legal), easing the burden of annotating messages.
            program = session.read(port)
            while program != ar.EOF:
                session.eval(program)
                program = session.read(port)

        except Exception as e:
            exception_warning = dict(
                message=str(e),
            )
            warnings.append(annotate_dict(annotation, exception_warning))

        assert state == session.state
        warnings.extend(annotate_dict(annotation, w) for w in session.warnings)
        events.extend(annotate_dict(annotation, e) for e in session.events)

    # Glue the results into a response.
    return dict(events=events, state=state, warnings=warnings)

# ## WEB APPLICATION CONFIGURATION

# Annotations have a set of related API routes, which will all be mounted
# together. The root of this collection is the state computation endpoint.

from apistar import Route

routes = [
    Route('', 'GET', compute_state),
]
