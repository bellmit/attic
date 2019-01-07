"""trigger Jenkins builds via Mercurial actions

Jenkins builds that support "Trigger Build Remotely" can be triggered using
this extension. To use this extension, you must provide the following
configuration items:

    [jenkins]
    url = https://builds.example.com/
    job = my-build-name
    token = A SECRET VALUE

This extension provides a command, 'hg build', for triggering a build
interactively, as well as a hook for triggering builds on commit or push.
"""

import urllib as ul
import urllib2 as ul2
import urlparse as up
import posixpath as path
from mercurial import cmdutil

cmdtable = {}
command = cmdutil.command(cmdtable)

class IncompleteConfig(Exception):
    pass

def build_url(url, job, token, cause=None):
    """Concocts the URL for triggering a build, given a Jenkins base URL,
    the job name, and the build token. This will also include the build cause
    if it's included and not false-like.
    """
    parts = up.urlsplit(url)
    
    urlpath = path.join(parts.path, 'job', job, 'build')
    
    query = up.parse_qs(parts.query, keep_blank_values=True)
    query['token'] = token
    if cause:
        query['cause'] = cause
    urlquery = ul.urlencode(query)
    
    return up.urlunsplit(parts._replace(path=urlpath, query=urlquery))

def trigger_build(url, job, token, cause=None):
    if url is None or job is None or token is None:
        raise IncompleteConfig
    
    ul2.urlopen(build_url(url, job, token, cause))

@command('build', [
    ('c', 'cause', '', 'include build cause message', 'CAUSE'),
    ('o', 'optional', False, 'silently ignore incomplete configuration')
])
def build(ui, repo, cause='', optional=False):
    """trigger a build in the configured Jenkins server
    
    If the '--cause' argument is provided, it will be passed along to Jenkins
    as the cause of the build.
    
    If build is run with '--optional', incomplete configuration will cause
    the build not to be submitted and the command will exit successfully.
    Otherwise, the command will fail if the build configuration is incomplete.
    
    You can set or override the Jenkins configuration using
    '--config jenkins.url=URL', '--config jenkins.job=JOB-NAME', and
    '--config jenkins.token=TOKEN' command-line options.
    """
    try:
        url = ui.config('jenkins', 'url', None)
        job = ui.config('jenkins', 'job', None)
        token = ui.config('jenkins', 'token', None)
        
        ui.debug('Building %r on server %r\n' % (job, url))
        trigger_build(url, job, token, cause)
    except IncompleteConfig:
        if optional:
            ui.note('Incomplete build config, no build scheduled.\n')
        else:
            ui.write('Incomplete build config.\n')
            return 3
    except ul2.HTTPError as e:
        ui.warn('Failed to submit build: %s\n' % e)
        return 2
    except ul2.URLError as e:
        ui.warn('Failed to submit build: %s\n' % e.reason)
        return 2

def buildhook(ui, repo, hooktype, **kwargs):
    """trigger a build in the configured Jenkins server
    
    This is appropriate for use as a 'commit' or 'changegroup' hook:
    
        [hooks]
        changegroup.buildhook = python:/path/to/buildhook.py:buildhook
    
    Hook-triggered builds are always optional: incomplete configuration will
    cause the hook to do nothing (other than logging a note).
    """
    try:
        url = ui.config('jenkins', 'url', None)
        job = ui.config('jenkins', 'job', None)
        token = ui.config('jenkins', 'token', None)
        
        ui.debug('Building %r on server %r\n' % (job, url))
        trigger_build(url, job, token, 'Triggered by %s' % (hooktype,))
    except IncompleteConfig:
        ui.note('Incomplete build config, no build scheduled.\n')

testedwith = "2.1.1"
