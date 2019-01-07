import contextlib as c

@c.contextmanager
def from_stream(stream):
    with c.closing(stream):
        yield stream.read()
