import urlparse
import posixpath

def join(api_url, *endpoints):
    scheme, netloc, path, query, fragment = urlparse.urlsplit(api_url)
    
    new_path = posixpath.join(path, *endpoints)
    
    return urlparse.urlunsplit((scheme, netloc, new_path, query, fragment))
