import json
import functools

from bugcli import rest
from bugcli import url
from bugcli import exception as exc

from bugcli.tracker import base

def open_issue_request(project_key, issue_type, summary, description):
    return dict(
        fields=dict(
            project=dict(
                key=project_key
            ),
            summary=summary,
            description=description,
            issuetype=dict(
                name=issue_type
            )
        )
    )

def new_comment_request(comment):
    return dict(
        body=comment
    )

def resolve_issue_request(comment, resolution, version, transition_id):
    fields = dict(
        resolution=dict(
            name=resolution
        )
    )
    if version is not None:
        fields['fixVersions'] = [
            dict(
                name=version
            )
        ]
    return dict(
        update=dict(
            comment=[
                dict(
                    add=dict(
                        body=comment
                    )
                )
            ]
        ),
        fields=fields,
        transition=dict(
            id=transition_id
        )
    )

class JiraError(exc.TrackerError):
    @classmethod
    def from_http_error(cls, e):
        errors = json.loads(e.response.text)
        messages = errors['errors'].values() + errors['errorMessages']
        if len(messages) == 1:
            return cls(messages[0], None)
        else:
            return cls(
                'The JIRA server returned errors',
                cls.format_messages(messages)
            )
    
    @classmethod
    def from_captcha_error(cls, captcha_url):
        return cls(
            'CAPTCHA verification required',
            'Please visit %s to continue.' % (captcha_url,)
        )
    
    @classmethod
    def format_messages(cls, messages):
        return ''.join(cls.format_message(m) for m in messages)
    
    @classmethod
    def format_message(cls, message):
        return ' * %s' % (message,)

def jira_errors(method):
    def parse_captcha_info(headers):
        captcha_header = headers['x-authentication-denied-reason']
        if captcha_header is None or not captcha_header.startswith('CAPTCHA_CHALLENGE'):
            return None
        return captcha_header[len('CAPTCHA_CHALLENGE; login-url='):]
    
    @functools.wraps(method)
    def wrapper(*args, **kwargs):
        try:
            return method(*args, **kwargs)
        except rest.HTTPError, e:
            r = e.response
            if r.status_code in (400, 404):
                raise JiraError.from_http_error(e)
            if r.status_code == 403:
                captcha_url = parse_captcha_info(r.headers)
                if captcha_url is not None:
                    raise JiraError.from_captcha_error(captcha_url)
            raise
    return wrapper

class Jira(base.TrackerOperations):
    @classmethod
    def from_config(cls, config):
        base_url = config.require('jira', 'url')
        project_key = config.require('jira', 'project')
        issue_type = config.get('jira', 'type')
        resolution = config.get('jira', 'resolution', 'Fixed')
        resolve_workflow = config.get('jira', 'resolve_workflow', 'Resolve Issue')
        return cls(base_url, project_key, issue_type, resolution, resolve_workflow)
    
    @classmethod
    def auth_section(cls, config):
        return config.get('jira', 'url')
    
    @classmethod
    def command_line_args(cls, parser):
        group = parser.add_argument_group('JIRA-specific configuration')
        group.add_argument(
            '-u', '--url',
            metavar='URL',
            help='The URL of your JIRA installation.'
        )
        group.add_argument(
            '-p', '--project',
            metavar='KEY',
            help='The JIRA key for your project.'
        )
    
    def __init__(self, base_url, project_key, default_issue_type, default_resolution, resolve_workflow):
        self.base_url = base_url
        self.project_key = project_key
        self.default_issue_type = default_issue_type
        self.default_resolution = default_resolution
        self.resolve_workflow = resolve_workflow
    
    @jira_errors
    def open_issue(self, auth, summary, description, issue_type=None):
        if issue_type is None:
            issue_type = self.default_issue_type
        if issue_type is None:
            raise ValueError('No issue type specified.')
        
        issue_request = open_issue_request(
            self.project_key,
            issue_type,
            summary,
            description
        )
        issue_response = rest.post(
            self.api_url('issue'),
            issue_request,
            auth.http_basic()
        )
        issue_key = issue_response['key']
        return issue_key, self.issue_url(issue_key)
    
    @jira_errors
    def comment_issue(self, auth, issue, comment):
        comment_request = new_comment_request(
            comment
        )
        rest.post(
            self.api_url('issue', issue, 'comment'),
            comment_request,
            auth.http_basic()
        )
        return issue, self.issue_url(issue)
    
    @jira_errors
    def resolve_issue(self, auth, issue, comment, resolution=None, version=None):
        if resolution is None:
            resolution = self.default_resolution
        
        resolve_transition = self.find_transition(auth, issue, self.resolve_workflow)
        
        resolve_request = resolve_issue_request(
            comment,
            resolution,
            version,
            resolve_transition
        )
        rest.post(
            self.api_url('issue', issue, 'transitions'),
            resolve_request,
            auth.http_basic()
        )
        return issue, self.issue_url(issue)
    
    def find_transition(self, auth, issue, workflow):
        transitions = rest.get(
            self.api_url('issue', issue, 'transitions'),
            auth.http_basic()
        )
        for transition in transitions['transitions']:
            if transition['name'] == workflow:
                return transition['id']
        raise JiraError('Unable to find transition named %r for %s' % (
            workflow, issue
        ))
    
    def issue_url(self, issue_key):
        return url.join(
            self.base_url,
            'browse',
            issue_key
        )

    def api_url(self, endpoint, *subparts):
        return url.join(self.base_url, 'rest/api/2', endpoint, *subparts)
