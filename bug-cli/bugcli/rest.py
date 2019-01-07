import json
import requests as http

# For export to other modules.
from requests import HTTPError

class JsonCodec(object):
    @classmethod
    def encode(cls, request):
        return json.dumps(request)
    
    request_type = 'application/json'
    response_type = 'application/json'
    
    @classmethod
    def decode(cls, response):
        return json.loads(response)

def get(url, auth, codec=JsonCodec):
    return request(http.get, url, None, auth, codec)

def post(url, message, auth, codec=JsonCodec):
    return request(http.post, url, message, auth, codec)

def put(url, message, auth, codec=JsonCodec):
    return request(http.put, url, message, auth, codec)

def request(method, url, message, auth, codec):
    data = None
    if message is not None:
        data = codec.encode(message)
    resp = method(
        url,
        data=data,
        auth=auth,
        headers={
            'Content-type': codec.request_type,
            'Accept': codec.response_type
        }
    )
    resp.raise_for_status()
    if resp.headers['Content-length'] != '0':
        return codec.decode(resp.text)
    return None
