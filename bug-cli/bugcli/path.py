import itertools
import errno
import os.path

def ancestors(path):
    visited = set()
    while path not in visited:
        visited.add(path)
        yield path
        path = os.path.dirname(path)

def first_ancestor_with(path, filename):
    for path in ancestors(path):
        candidate = os.path.join(path, filename)
        if os.path.exists(candidate):
            return candidate
    return None

def derived_filenames(basename):
    yield basename
    for suffix in itertools.count(1):
        yield '%s.%s' % (basename, suffix)

def open_first_unused_file(dir, basename):
    for name in derived_filenames(basename):
        path = os.path.join(dir, name)
        try:
            return path, os.open(path, os.O_CREAT | os.O_EXCL | os.O_RDWR)
        except OSError, e:
            if e.errno != errno.EEXIST:
                raise
