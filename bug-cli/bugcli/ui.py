import contextlib as c
import subprocess
import os
import os.path
import getpass
import sys
import itertools
import errno

from bugcli import editor
from bugcli import path as p

DESCRIPTION_TEMPLATE = """
--: This is your issue description. Lines prefixed with --: will be removed.
--: When you're done, save this file and exit your editor.
"""

COMMENT_TEMPLATE = """
--: This is your comment. Lines prefixed with --: will be removed. When you're
--: done, save this file and exit your editor.
"""

def read_content(path):
    def noncomment_lines(file):
        for line in file:
            if not line.startswith('--:'):
                yield line
    
    with open(path, 'rt') as file:
        return ''.join(noncomment_lines(file)).strip()

class Terminal(object):
    def prompt_tracker_password(self):
        return getpass.getpass()
    
    def prompt_issue_description(self):
        return self.preservably_editable_file(
            os.getcwd(),
            'bug-cli.tmp',
            DESCRIPTION_TEMPLATE
        )
    
    def prompt_comment(self):
        return self.preservably_editable_file(
            os.getcwd(),
            'bug-cli.tmp',
            COMMENT_TEMPLATE
        )
    
    def show_issue_change(self, key, url, change):
        print "%s [%s] %s." % (key, url, change)
    
    def show_issue_opened(self, key, url):
        self.show_issue_change(key, url, 'opened')
    
    def show_issue_updated(self, key, url):
        self.show_issue_change(key, url, 'updated')
    
    def show_issue_resolved(self, key, url):
        self.show_issue_change(key, url, 'resolved');
    
    def show_tracker_error(self, exc):
        print >>sys.stderr, "%s: %s" % (
            os.path.basename(sys.argv[0]),
            exc.message
        )
        if exc.details is not None:
            print >>sys.stderr, exc.details
    
    def report_file_preserved(self, path):
        print >>sys.stderr, "%s: Your message has been preserved in %s" % (
            os.path.basename(sys.argv[0]),
            path
        )
    
    @c.contextmanager
    def preservably_editable_file(self, dir, basename, initial_content=None):
        path, fd = p.open_first_unused_file(dir, basename)
        try:
            editor.launch(path, initial_content)
            yield read_content(path)
        except:
            self.report_file_preserved(path)
            raise
        else:
            os.unlink(path)
        finally:
            os.close(fd)
    