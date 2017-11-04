from urllib.parse import urljoin, urlparse

class Urls(object):
    def __init__(self, base_url):
        self.base_url = base_url

    def join(self, path):
        return urljoin(self.base_url, path)

    @property
    def host(self):
        parts = urlparse(self.base_url)
        netloc_parts = parts.netloc.split(':', 1)
        return netloc_parts[0]

    @property
    def documents(self):
        return urljoin(self.base_url, 'document')

    @property
    def user(self):
        return urljoin(self.base_url, 'user')

    @property
    def register_user(self):
        return urljoin(self.base_url, 'user/register')

    @property
    def issue_token(self):
        return urljoin(self.base_url, 'user/token')

    @property
    def tokens(self):
        return urljoin(self.base_url, 'user/token')

    def revoke_token(self, id):
        return urljoin(self.base_url, f'user/token/{id}')
