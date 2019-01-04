from __future__ import absolute_import

import io
import fcntl

def wait_open(name, mode, lock=fcntl.LOCK_SH):
    fd = open(name, mode)
    try:
        fcntl.lockf(fd, lock)
        return fd
    except:
        fd.close()
        raise

def seek_end(fd):
    fd.seek(0, io.SEEK_END)
