"""WSGI Middleware and Paster glue for profiling webapps."""

import sys
from os.path import join
from cProfile import Profile
from pstats import Stats
from functional import partial
from datetime import datetime

__all__ = ['show_stats', 'ProfileStore', 'ProfiledApp', 'filter']

def show_stats(args=sys.argv[1:]):
    """Prints human-readable profile information to ``stdout`` for each
    profile data file listed in ``args``.
    
    :param args: the profile data files to load. Defaults to ``sys.argv[1:]``.
    """
    for stats in [Stats(filename) for filename in args]:
        stats.print_stats()

def filter(global_conf, profile_dir):
    """A Paster filter for profiling applications.
    
    .. highlight:: ini
    
    To apply this to an application, use Paster's ``filter-with`` attribute ::
    
        [filter:profile]
        use = egg:profileware#profile
        profile_dir = %(here)s/profile
        
        [app:main]
        # ...
        filter-with = profile
    
    For each request, the app will write a file to ``./profile/`` that can be
    read out using the included ``profcat`` command.
    """
    store = ProfileStore(profile_dir)
    return partial(ProfiledApp, store)


class ProfileStore(object):
    """Stores Profile objects out to the filesystem. Every profile is written
    out to a file named according to the current time, in the format used by
    ``Profile.dump_stats``.
    
    The resulting files can be read by the included ``profcat`` command.
    
    :param dir: profiles will be saved to this directory, as separate ``.profile``
        files.
    """
    
    def __init__(self, dir):
        self.dir = dir
    
    def __call__(self, profile):
        destination = self.generate_filename()
        profile.dump_stats(destination)
    
    def generate_filename(self):
        """Generates the destination filename for a profile. The generated
        names are of the form ``YYYY-MM-DD-HH:MM:SS.SSSSS.profile``.
        """
        now = datetime.utcnow()
        return join(self.dir, '%d-%02d-%02d-%02d:%02d:%02d.%06d.profile' % (
            now.year,
            now.month,
            now.day,
            now.hour,
            now.minute,
            now.second,
            now.microsecond
        ))

class ProfiledApp(object):
    """A WSGI middleware that profiles applications.
    
    .. highlight:: python
    
    This middleware uses a store callable (for example, instances of the
    ``ProfileStore`` class) to store profile information for each WSGI
    invocation.
    
    :param store: a callable that stores a single profile result.
    :param app: the WSGI app to profile.
    """
    
    def __init__(self, store, app):
        self.store = store
        self.app = app
    
    def __call__(self, *args, **kwargs):
        profiler = Profile()
        
        try:
            rv = profiler.runcall(lambda: self.app(*args, **kwargs))
            iter = profiler.runcall(rv.__iter__)
            try:
                while True:
                    yield profiler.runcall(iter.next)
            except StopIteration:
                pass
        finally:
            self.store(profiler)
