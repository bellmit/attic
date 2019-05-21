from . import context as c
from . import localization as l
from . import structures as s

class GameData:
    def __init__(self, context):
        self.context = context
        self.localization = l.from_context(context)
        self.components = s.Components.from_context(context)
        self.physical_items = s.PhysicalItems.from_context(context)
        self.blueprints = s.Blueprints.from_context(context)
        self.blocks = s.Blocks.from_context(context)

    @classmethod
    def from_directory(cls, basedir):
        return cls(c.Context(basedir))
