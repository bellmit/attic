import urllib

from bugcli import rest
from bugcli import url
from bugcli.tracker import base

class WackyBitbucketProtocolCodec(rest.JsonCodec):
    @classmethod
    def encode(cls, request):
        return urllib.urlencode(request)
    
    request_type = 'application/x-www-form-urlencoded'

def open_issue_request(issue_type, summary, description):
    return dict(
        title=summary,
        content=description,
        kind=issue_type
    )

def new_comment_request(comment):
    return dict(
        content=comment
    )

def resolve_issue_request(resolution, version):
    request = dict(
        status=resolution
    )
    if version is not None:
        request['version'] = version
    return request

class Bitbucket(base.TrackerOperations):
    @classmethod
    def from_config(cls, config):
        user = config.require('bitbucket', 'user')
        project = config.require('bitbucket', 'project')
        issue_type = config.get('bitbucket', 'type')
        resolution = config.get('bitbucket', 'resolution', 'resolved')
        api_url = config.get('bitbucket', 'api_url', 'https://api.bitbucket.org/1.0/repositories/')
        return cls(user, project, issue_type, resolution, api_url)
    
    @classmethod
    def auth_section(cls, config):
        user = config.get('bitbucket', 'user')
        project = config.get('bitbucket', 'project')
        return cls.repo_url(user, project)
    
    @classmethod
    def repo_url(cls, user, project):
        return "https://bitbucket.org/%s/%s" % (user, project)
    
    @classmethod
    def command_line_args(cls, parser):
        group = parser.add_argument_group('Bitbucket-specific configuration')
        group.add_argument(
            '-u', '--user',
            metavar='USERNAME',
            help='The username of the repository owner.'
        )
        group.add_argument(
            '-p', '--project',
            metavar='PROJECT',
            help='The bitbucket repository name.'
        )
    
    def __init__(self, user, project, default_issue_type, default_resolution, api_url):
        self.user = user
        self.project = project
        self.default_issue_type = default_issue_type
        self.default_resolution = default_resolution
        self.api_base_url = api_url
    
    def open_issue(self, auth, summary, description, issue_type=None):
        if issue_type is None:
            issue_type = self.default_issue_type
        if issue_type is None:
            raise ValueError('No issue type specified.')
        
        issue_request = open_issue_request(
            issue_type,
            summary,
            description
        )
        issue_response = rest.post(
            self.api_url('issues'),
            issue_request,
            auth.http_basic(),
            codec=WackyBitbucketProtocolCodec
        )
        issue_id = issue_response['local_id']
        return issue_id, self.issue_url(issue_id)
    
    def comment_issue(self, auth, issue, comment):
        comment_request = new_comment_request(
            comment
        )
        rest.post(
            self.api_url('issues', issue, 'comments'),
            comment_request,
            auth.http_basic(),
            codec=WackyBitbucketProtocolCodec
        )
        return issue, self.issue_url(issue)
    
    def resolve_issue(self, auth, issue, comment, resolution=None, version=None):
        if resolution is None:
            resolution = self.default_resolution
        
        resolve_request = resolve_issue_request(resolution, version)
        rest.put(
            self.api_url('issues', issue, ''), 
            resolve_request,
            auth.http_basic(),
            codec=WackyBitbucketProtocolCodec
        )
        return self.comment_issue(auth, issue, comment)
    
    def issue_url(self, issue_key):
        return url.join(
            self.repo_url(self.user, self.project),
            'issue',
            str(issue_key)
        )

    def api_url(self, endpoint, *subparts):
        return url.join(
            self.api_base_url,
            self.user,
            self.project,
            endpoint,
            *subparts
        )
