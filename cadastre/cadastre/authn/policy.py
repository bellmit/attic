# Authorization policies used in Cadastre follow one of a small number of
# schemes.
#
# The default scheme allows any request, including unauthenticated requests. To
# use this scheme, no additional code is required.

# The Authenticated scheme requires that a user present any kind of valid
# credential.
from apistar.interfaces import Auth

class Authenticated(object):
    def has_permission(self, auth: Auth):
        return auth.is_authenticated()

# This scheme may be imposed on an API endpoint using the `@authenticated`
# decorator.

from apistar import annotate

def authenticated(endpoint):
    return annotate(
        permissions=[Authenticated()]
    )(endpoint)

# The Interactive policy requires that a user present a password credential.
# This restricts the operation such that API token clients cannot perform it.
from apistar.interfaces import Auth

class Interactive(object):
    def has_permission(self, auth: Auth):
        return auth.is_authenticated() and auth.token is None

# This scheme may be imposed on an API endpoint using the `@interactive`
# decorator.

from apistar import annotate

def interactive(endpoint):
    return annotate(
        permissions=[Interactive()]
    )(endpoint)
