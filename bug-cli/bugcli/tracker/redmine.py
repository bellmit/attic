from bugcli import rest
from bugcli import url

from bugcli.tracker import base

class RedmineIsBadAtJsonCodec(rest.JsonCodec):
    @classmethod
    def decode(cls, response):
        # Sometimes, Redmine returns whitespace-only strings as "JSON".
        if response.strip():
            return super(RedmineIsBadAtJsonCodec, cls).decode(response)
        return None

def open_issue_request(project, issue_type, summary, description):
    return dict(
        issue=dict(
            project_id=project,
            tracker_name=issue_type,
            subject=summary,
            description=description
        )
    )

def new_comment_request(comment):
    return dict(
        issue=dict(
            notes=comment
        )
    )

def resolve_issue_request(comment, resolution_id, version_id):
    request = dict(
        issue=dict(
            notes=comment,
            status_id=resolution_id,
        )
    )
    if version_id is not None:
        request['issue']['fixed_version_id'] = version_id
    return request

class Redmine(base.TrackerOperations):
    @classmethod
    def from_config(cls, config):
        base_url = config.require('redmine', 'url')
        project_key = config.require('redmine', 'project')
        issue_type = config.get('redmine', 'type', 'task')
        resolution = config.get('redmine', 'resolution', 'Closed')
        return cls(base_url, project_key, issue_type, resolution)
    
    @classmethod
    def auth_section(cls, config):
        return config.get('redmine', 'url')
    
    @classmethod
    def command_line_args(cls, parser):
        group = parser.add_argument_group('Redmine-specific configuration')
        group.add_argument(
            '-u', '--url',
            metavar='URL',
            help='The URL of your Redmine installation.'
        )
        group.add_argument(
            '-p', '--project',
            metavar='ID',
            help='The project ID for your project.'
        )
    
    def __init__(self, base_url, project_key, default_issue_type, default_resolution):
        self.base_url = base_url
        self.project_key = project_key
        self.default_issue_type = default_issue_type
        self.default_resolution = default_resolution
    
    def open_issue(self, auth, summary, description, issue_type=None):
        if issue_type is None:
            issue_type = self.default_issue_type
        
        issue_request = open_issue_request(
            self.project_key,
            issue_type,
            summary,
            description
        )
        issue_response = rest.post(
            self.api_url('issues.json'),
            issue_request,
            auth.http_basic(),
            codec=RedmineIsBadAtJsonCodec
        )
        issue_id = issue_response['issue']['id']
        return issue_id, self.issue_url(issue_id)
    
    def comment_issue(self, auth, issue, comment):
        comment_request = new_comment_request(
            comment
        )
        rest.put(
            self.api_url('issues', '%s.json' % (issue,)),
            comment_request,
            auth.http_basic(),
            codec=RedmineIsBadAtJsonCodec
        )
        return issue, self.issue_url(issue)
    
    def resolve_issue(self, auth, issue, comment, resolution=None, version=None):
        if resolution is None:
            resolution = self.default_resolution
        
        version_id = None
        if version is not None:
            version_id = self.find_version_id(auth, version)
        resolution_id = self.find_resolution_id(auth, resolution)
        
        resolve_request = resolve_issue_request(
            comment,
            resolution_id,
            version_id
        )
        rest.put(
            self.api_url('issues', '%s.json' % (issue,)),
            resolve_request,
            auth.http_basic(),
            codec=RedmineIsBadAtJsonCodec
        )
        return issue, self.issue_url(issue)
    
    def find_id(self, auth, uri, collection, name, fail_message):
        candidates = rest.get(
            uri,
            auth.http_basic(),
            codec=RedmineIsBadAtJsonCodec
        )
        for candidate in candidates[collection]:
            if candidate['name'] == name:
                return candidate['id']
        raise ValueError(fail_message % (name,))
    
    def find_version_id(self, auth, version_name):
        return self.find_id(
            auth,
            self.api_url('projects', self.project_key, 'versions.json'),
            'versions',
            version_name,
            'Unknown version %r'
        )
    
    def find_resolution_id(self, auth, resolution):
        return self.find_id(
            auth,
            self.api_url('issue_statuses.json'),
            'issue_statuses',
            resolution,
            'Unknown resolution %r'
        )
    
    def issue_url(self, issue_key):
        return url.join(self.base_url, 'issues', str(issue_key))
    
    def api_url(self, endpoint, *subparts):
        return url.join(self.base_url, endpoint, *subparts)
