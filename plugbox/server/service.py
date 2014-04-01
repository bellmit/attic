import contextlib as cx
import errno
import os
import os.path as p
import shutil as sh
import subprocess as sub

def allow_dir(path):
    def allow_dir_check(e):
        return e.filename == path and p.isdir(path)
    return allow_dir_check

def allow_symlink(path):
    def allow_symlink_check(e):
        return e.filename is None and p.islink(path)
    return allow_symlink_check

@cx.contextmanager
def ignore_existing(check):
    try:
        yield
    except OSError as e:
        if e.errno != errno.EEXIST:
            raise
        if not check(e):
            raise

def replace_symlink(target, link):
    lock = "{link}.lock".format(**locals())
    os.symlink(target, lock)
    os.rename(lock, link)

class VersionProperty(object):
    def __init__(self, link_attribute):
        self.link_attribute = link_attribute

    def __get__(self, obj, target=None):
        try:
            link = getattr(obj, self.link_attribute)
            version_name = os.readlink(link)
            return obj.version(version_name)
        except OSError as e:
            if e.errno == errno.ENOENT:
                return None
            raise

    def __set__(self, obj, version):
        link = getattr(obj, self.link_attribute)
        if version is None:
            os.remove(link)
        else:
            assert version.deploydir == obj.deploy_dir
            replace_symlink(version.name, link)

class Service(object):
    DEPLOY_DIR = 'deploy'
    CONFIG_DIR = 'config'
    
    SERVICE_DIRS = [
        DEPLOY_DIR,
        CONFIG_DIR,
    ]

    LIVE_SYMLINK = 'live'
    NEXT_SYMLINK = 'next'
    PREVIOUS_SYMLINK = 'previous'

    def __init__(self, basedir):
        self.basedir = basedir
        self.deploy_dir            = p.join(self.basedir,    self.DEPLOY_DIR)
        self.service_dirs         = [p.join(self.basedir,    dir) for dir in self.SERVICE_DIRS]
        self.live_link             = p.join(self.deploy_dir, self.LIVE_SYMLINK)
        self.next_link             = p.join(self.deploy_dir, self.NEXT_SYMLINK)
        self.previous_link         = p.join(self.deploy_dir, self.PREVIOUS_SYMLINK)
        self.live_convenience_link = p.join(self.basedir,    self.LIVE_SYMLINK)

    def init(self):
        self._make_service_root()
        self._make_service_dirs()
        self._make_live_convenience_symlink()

    def _make_service_root(self):
        with ignore_existing(allow_dir(self.basedir)):
            os.makedirs(self.basedir)

    def _make_service_dirs(self):
        for dir in self.service_dirs:
            self._make_service_dir(dir)

    def _make_service_dir(self, dir):
        with ignore_existing(allow_dir(dir)):
            os.makedirs(dir)

    def _make_live_convenience_symlink(self):
        live_convenience_target = p.join(self.DEPLOY_DIR, self.LIVE_SYMLINK)
        with ignore_existing(allow_symlink(self.live_convenience_link)):
            os.symlink(live_convenience_target, self.live_convenience_link)

    next     = VersionProperty('next_link')
    live     = VersionProperty('live_link')
    previous = VersionProperty('previous_link')

    def create_next_version(self, name):
        v = Version(self.deploy_dir, name)
        v.create()
        self.next = v
        return v

    def clone_next_version(self, name):
        v = Version(self.deploy_dir, name)
        v.clone(self.live.dir)
        self.next = v
        return v

    def upgrade_to(self, version_name):
        version = self.version(version_name)
        live = self.live
        next = self.next
        if version != next:
            raise NotExpectedVersion(version)
        self.activate(live, next)

    def rollback_to(self, version_name):
        version = self.version(version_name)
        live = self.live
        if live and not live.check_rollback(version):
            raise NotExpectedVersion(version)
        self.activate(version)

    def activate(self, live, next):
        if live is not None:
            replace_symlink(live.name, self.previous_link)
        replace_symlink(next.name, self.live_link)
        os.remove(self.next_link)

    def remove_version(self, version):
        v = self.version(version)
        if v is None:
            raise NoSuchVersion(version)
        live = self.live
        if v == live:
            raise RemovingLiveVersion(v)
        self.remove(v)

    def remove(self, version):
        if self.next == version:
            os.remove(self.next_link)
        if self.previous == version:
            os.remove(self.previous_link)
        assert self.live != version
        version.remove()

    def remove_previous(self):
        previous = self.previous
        if previous is None:
            raise NoSuchVersion('previous')
        if previous == self.live:
            raise RemovingLiveVersion(previous)
        self.remove(previous)

    def remove_next(self):
        next = self.next
        if next is None:
            raise NoSuchVersion('next')
        if next == self.live:
            raise RemovingLiveVersion(next)
        self.remove(next)

    def purge(self):
        keep = [self.previous, self.live, self.next]
        for target in self.versions():
            if not target in keep:
                self.remove(target)

    def versions(self):
        candidates = sorted(os.listdir(self.deploy_dir))
        version_names = [c for c in candidates if self._is_version_name(c)]
        return [Version(self.deploy_dir, name) for name in version_names]

    def _is_version_name(self, name):
        deploy_path = p.join(self.deploy_dir, name)
        return p.isdir(deploy_path) and not p.islink(deploy_path)

    def version(self, name):
        v = Version(self.deploy_dir, name)
        if v.exists():
            return v
        return None

class NotExpectedVersion(Exception):
    def __init__(self, version):
        super(NotExpectedVersion, self).__init__('Specified version is not the expected target version: {version.name}'.format(**locals()))

class RemovingLiveVersion(Exception):
    def __init__(self, version):
        super(RemovingLiveVersion, self).__init__('Specified version is the live version: {version.name}'.format(**locals()))

class NoSuchVersion(Exception):
    def __init__(self, version_name):
        super(NoSuchVersion, self).__init__('Version does not exist: {version_name}'.format(**locals()))

class Version(object):
    def __init__(self, deploydir, name):
        self.deploydir = deploydir
        self.name = name
        self.dir = p.join(self.deploydir, self.name)

    def create(self):
        os.mkdir(self.dir)

    def clone(self, source):
        if source[-1] != '/':
            source = source + '/' # my little rsync: slashes are magic
        sub.check_call(['rsync', '-a', '--del', source, self.dir])

    def exists(self):
        return p.isdir(self.dir)

    def remove(self):
        sh.rmtree(self.dir)

    def __eq__(self, other):
        if other is None:
            return False;
        return (self.deploydir, self.name) == (other.deploydir, other.name)

    def __ne__(self, other):
        return not self == other

