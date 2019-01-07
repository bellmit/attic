"""
Tracker for GitHub.
Kit La Touche, 2012-03-28
Version 0.1
"""

from bugcli import rest, url
from bugcli.tracker import base

class GitHub(base.TrackerOperations):
    """
    Entry point for GitHub tracker interface.
    """
    
    @classmethod
    def command_line_args(cls, parser):
        group = parser.add_argument_group('GitHub-specific configuration')
        group.add_argument(
            '-u', '--user',
            metavar='USERNAME',
            help='The username of the repository owner.'
        )
        group.add_argument(
            '-p', '--project',
            metavar='PROJECT',
            help='The github repository name.'
        )
    
    @classmethod
    def auth_section(cls, config):
        user = config.get('github', 'user')
        project = config.get('github', 'project')
        return cls.repo_url(user, project)
    
    @classmethod
    def from_config(cls, config):
        user = config.require('github', 'user')
        project = config.require('github', 'project')
        api_url = config.get('github', 'api_url', 'https://api.github.com/')
        return cls(user, project, api_url)

    @classmethod
    def repo_url(cls, user, project):
        return url.join(
            "https://github.com/",
            cls.repo_segment(user, project)
        )
    
    @classmethod
    def repo_segment(cls, user, project):
        return url.join(user, project)

    def __init__(self, user, project, api_url):
        self.user = user
        self.project = project
        self.api_base_url = api_url 

    def open_issue(self, auth, summary, description, issue_type=None):
        issue_request = {'title': summary, 'body': description}
        issue_response = rest.post(
            self.api_url('issues'),
            issue_request,
            auth.http_basic()
        )
        issue_id = issue_response['number']
        return issue_id, self.issue_url(issue_id)
    
    def comment_issue(self, auth, issue, comment):
        comment_request = {'body': comment}
        rest.post(
            self.api_url('issues', issue, 'comments'),
            comment_request,
            auth.http_basic()
        )
        return issue, self.issue_url(issue)
    
    def resolve_issue(self, auth, issue, comment, resolution=None, version=None):
        close_request = {'state': 'closed'}
        if version is not None:
            close_request['milestone'] = self.milestone_id(auth, version)
        rest.post(
            self.api_url('issues', issue),
            close_request,
            auth.http_basic()
        )
        return issue, self.issue_url(issue)
    
    def milestone_id(self, auth, version):
        milestones = rest.get(
            self.api_url('milestones'),
            auth.http_basic()
        )
        for milestone in milestones:
            if milestone['title'] == version:
                return milestone['number']
        raise ValueError('Unknown version %r', version)
    
    def api_url(self, *subparts):
        return url.join(
            self.api_base_url,
            'repos',
            self.repo_segment(self.user, self.project),
            *subparts
        )
    
    def issue_url(self, issue):
        return url.join(
            'https://github.com/',
            self.user,
            self.project,
            'issues',
            str(issue)
        )
