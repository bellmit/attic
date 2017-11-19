# ## ACTINIDE SESSIONS
#
# This session class automatically loads the builtins needed to manipulate
# Cadastre states.

import actinide

from . import builtins

class Session(actinide.Session):
    def __init__(self, state, *args, **kwargs):
        self._state = builtins.State(state)
        self._warnings = builtins.Warnings()
        self._events = builtins.Events()
        super().__init__(*args, **kwargs)

    def standard_library(self):
        super().standard_library()
        self.bind_module(builtins)
        self.bind_module(self._state)
        self.bind_module(self._warnings)
        self.bind_module(self._events)

    @property
    def state(self):
        return self._state.state

    @property
    def warnings(self):
        return self._warnings.warnings

    @property
    def events(self):
        return self._events.events
