from urllib.parse import urljoin, urlparse, urlsplit, urlunsplit, urlencode, parse_qsl, quote

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

    def get_documents(self, annotated):
        def annotated_params():
            if annotated is not None:
                if annotated:
                    yield ('annotated', 'true')
                else:
                    yield ('annotated', '')

        base = urljoin(self.base_url, 'document')
        parts = urlsplit(base)
        params = parse_qsl(parts.query) + list(annotated_params())
        query = urlencode(params, quote_via=quote)
        return urlunsplit((parts.scheme, parts.netloc, parts.path, query, parts.fragment))

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
