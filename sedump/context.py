import lxml.etree as etree
import os.path as p

class Context:
    def __init__(self, basedir):
        self.basedir = basedir

    def open(self, path, *args, **kwargs):
        # Opens a file in the context.
        path = p.join(self.basedir, path)
        return open(path, *args, **kwargs)

    def xml(self, path):
        with self.open(path, 'r', encoding='UTF-8') as f:
            return etree.parse(f)
