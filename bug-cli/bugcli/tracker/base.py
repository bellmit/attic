class TrackerOperations(object):
    def open_issue(self, auth, summary, description, issue_type=None):
        raise NotImplementedError('This tracker does not support opening issues.')
    
    def comment_issue(self, auth, issue, comment):
        raise NotImplementedError('This tracker does not support issue comments.')
    
    def resolve_issue(self, auth, issue, comment, resolution=None, version=None):
        raise NotImplementedError('This tracker does not support resolving issues.')
