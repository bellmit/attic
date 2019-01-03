# Cadastre verifies credentials in one of three ways, as described at the root
# of this section.

# Username and password credentials can be presented using Basic authentication.
# In this case, the returned auth object will have no token.

import base64
from apistar import http
from apistar.authentication import Authenticated
from . import repository as repo

class BasicPasswordAuthentication():
    def authenticate(self, authorization: http.Header, repository: repo.Repository):
        if authorization is None:
            return None

        scheme, token = authorization.split(' ', 1)
        if scheme.lower() != 'basic':
            return None

        username, password = base64.b64decode(token).decode('utf-8').split(':', 1)
        user = repository.find_user(username)
        if user is None:
            return None

        if user.validate_password(password):
            return Authenticated(username, user = user, token = None)

        return None

# API token credentials can be presented using Basic authentication with an
# empty username, or with a username exactly equal to the username associated
# with the token. In this case, the returned auth object will include the token.

import base64
from apistar import http
from apistar.authentication import Authenticated
from . import repository as repo

class BasicTokenAuthentication():
    def authenticate(self, authorization: http.Header, repository: repo.Repository):
        if authorization is None:
            return None

        scheme, token = authorization.split(' ', 1)
        if scheme.lower() != 'basic':
            return None

        username, password = base64.b64decode(token).decode('utf-8').split(':', 1)
        user = repository.find_token_user(password)
        if user is None:
            return None

        if username != '' and username != user.email:
            return None

        return Authenticated(user.email, user = user, token = password)

# API token credentials can be presented using Bearer authentication. In this
# case, the returned auth object will include the token.

from apistar import http
from apistar.authentication import Authenticated
from . import repository as repo

class BearerTokenAuthentication():
    def authenticate(self, authorization: http.Header, repository: repo.Repository):
        if authorization is None:
            return None

        scheme, token = authorization.split(' ', 1)
        if scheme.lower() != 'bearer':
            return None

        user = repository.find_token_user(token)
        if user is None:
            return None

        user = repository.find_token_user(token)
        if user is None:
            return None

        return Authenticated(user.email, user = user, token = token)

# To apply authentication to an applicatio using this authentication policy, use
# the following prefabricated list of authenticators.

all_authenticators = [
    BasicPasswordAuthentication(),
    BasicTokenAuthentication(),
    BearerTokenAuthentication(),
]
