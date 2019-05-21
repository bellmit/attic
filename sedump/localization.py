class Localization:
    def __init__(self, entries):
        self.entries = entries

    @classmethod
    def from_context(cls, context):
        texts = context.xml('Localization/MyTexts.resx')
        entries = texts.xpath(r'/root/data')
        pairs = (
            (*entry.xpath(r'@name'), entry.xpath(r'value/text()'))
            for entry in entries
        )
        localized_strings = {
            # One single, solitary entry in the game data, as of 1.190.1, has no
            # text. This hack deals with zero, one, and infinity texts cleanly.
            #
            # (It's ProgrammableBlock_OpenInWorkshop if you want to find it for
            # yourself.)
            name: ''.join(values)
            for (name, values) in pairs
        }
        return cls(localized_strings)

    def localize(self, name):
        try:
            return self.entries[name]
        except KeyError:
            return r'!!! {name} !!!'.format(name=name)

def from_context(context):
    return Localization.from_context(context)
