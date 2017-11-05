from urllib.parse import urljoin, urlparse, urlsplit, urlunsplit, urlencode, parse_qsl, quote

def add_params(url, additional_params):
    parts = urlsplit(url)
    params =parse_qsl(parts.query) + list(additional_params)
    query = urlencode(params, quote_via=quote)
    return urlunsplit((parts.scheme, parts.netloc, parts.path, query, parts.fragment))

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

    def document(self, message_id):
        return urljoin(self.base_url, 'document/{0}'.format(message_id))

    def get_documents(self, annotated):
        def annotated_params():
            if annotated is not None:
                if annotated:
                    yield ('annotated', 'true')
                else:
                    yield ('annotated', '')

        return add_params(self.documents, annotated_params())

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

    def state(self, before):
        def before_params():
            if before is not None:
                yield ('before', before.isoformat())

        return add_params(urljoin(self.base_url, 'state'), before_params())
