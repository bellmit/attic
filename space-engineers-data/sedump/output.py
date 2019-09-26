import os.path

class Output:
    def __init__(self, basedir):
        self.basedir = basedir

    def open(self, path, mode, *args, **kwargs):
        path = os.path.join(self.basedir, path)
        if 'w' in mode or 'a' in mode:
            self.ensure(path)
        return open(path, mode, *args, **kwargs)

    def ensure(self, path):
        dir = os.path.dirname(path)
        os.makedirs(dir, exist_ok=True)
