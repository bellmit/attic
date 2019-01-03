# Some operations require that the client present an authentication token. This
# is primarily to minimize abuse: by requiring a registration step, abusive
# users can be located and their contributions to the registry identified.
#
# ## TERMINOLOGY
#
# A user is a person who has registered. A user is identified by their email
# address. During the registration process, the person provides a password,
# which can be used to generate one or more bearer tokens for API usage.
#
# ## PRINCIPLES
#
# This authentication system operates in two modes. When the user is present,
# they can (and should) authenticate using their password. Password
# authentication verifies the user based on a secret only they know.
#
# To ensure that users are not encouraged to write down passwords, automated use
# of the API - including use through interactive tools - should use tokens,
# instead. Tokens are ephemeral: new tokens can be minted for an account very
# easily, and existing tokens invalidated quickly. This allows a user to take
# fine-grained control over the tokens available at any give time: for example,
# a token that has been leaked in logs or accidentally pasted can be invalidated
# without also rotating every other credential tied to the account.
#
# Authentication credentials can be presented to the service in the following
# ways:
#
# * Username and password credentials can be presented using HTTP Basic
#   authentication.
#
# * Token credentials can be presented using HTTP Basic authentication with an
#   empty username, or with a username exactly matching the token's owning user.
#
# * Token credentials can be presented using HTTP Bearer authentication.
#
# Certain API requests for managing tokens require username and password
# credentials. This prevents an API token from being used to, for example,
# change the user's password, or to issue new tokens that might then not be
# invalidated when the original token is invalidated.
#
# ## ENDPOINTS

# Registers a user. A person can call this endpoint to provide credentials. If
# the email address is not already registered, this will succeed, and return an
# initial API token for the user. If the email address is already registered,
# this will return a 400 Bad Request error with an informative body.

import apistar
from apistar import typesystem, reverse_url
from . import repository as repo

class Email(typesystem.String):
    format = 'email'
    pattern = '.+@.+'

class User(typesystem.Object):
    properties = {
        'email': Email,
        'password': typesystem.string(min_length=8),
        'token_description': typesystem.string(min_length=1),
    }

class Token(typesystem.Object):
    properties = {
        'id': typesystem.string(),
        'description': typesystem.string(),
        'token': typesystem.string(),
        'url': typesystem.string(format='url'),
    }

def register_user(user: User, repository: repo.Repository) -> Token:
    registered_user = repository.create_user(user['email'], user['password'])
    token = registered_user.generate_token(user['token_description'])
    return Token(
        id=token.id,
        description=token.description,
        token=token.token,
        url=reverse_url('revoke_token', id=token.id)
    )

# Changes a user's password. This is an interactive-only request - that the
# requesting user knows their current password is necessary to prove that they
# should be allowed to change it. This has no effect on outstanding API tokens.

from apistar.interfaces import Auth
from . import policy

class PasswordChange(typesystem.Object):
    properties = {
        'password': typesystem.string(min_length=8),
    }

@policy.interactive
def change_password(request: PasswordChange, auth: Auth):
    auth.user.update_password(request['password'])

# Returns some basic information about the current user. This is primarily
# intended to allow clients to correctly determine the logged-in user's email,
# for use when prompting for passwords for interactive authentication. It can be
# performed by any authenticated client, including clients using token
# authentication.

from apistar.interfaces import Auth
from . import policy

class WhoAmI(typesystem.Object):
    properties = {
        'email': Email,
    }

@policy.authenticated
def whoami(auth: Auth) -> WhoAmI:
    return WhoAmI(email = auth.user.email)

# Lists active tokens for an existing user. Like all token management, this is
# an interactive-only request. API tokens are not privileged to inspect one
# another (or, even, themselves) and cannot be used to extend access.

from apistar import reverse_url
from apistar.interfaces import Auth
import typing
from . import policy

@policy.interactive
def tokens(auth: Auth) -> typing.List[Token]:
    tokens = auth.user.tokens
    return [
        Token(
            id=token.id,
            description=token.description,
            url=reverse_url('revoke_token', id=token.id)
        )
        for token in tokens
    ]

# Issues a new token for an existing user. Like all token management, this is an
# interactive-only request. API tokens are not privileged to inspect one another
# (or, even, themselves) and cannot be used to extend access.
#
# This can be used to implement a "login" flow, where the user exchanges their
# password for an API token for the current client.

from apistar import reverse_url
from apistar.interfaces import Auth
from . import policy

class TokenRequest(typesystem.Object):
    properties = {
        'description': typesystem.string(min_length=1),
    }

@policy.interactive
def issue_token(token_request: TokenRequest, auth: Auth) -> Token:
    token = auth.user.generate_token(token_request['description'])
    return Token(
        id=token.id,
        description=token.description,
        token=token.token,
        url=reverse_url('revoke_token', id=token.id),
    )

# Revokes a token for an existing user. This immediately and irreversibly
# destroys the token: if the user wants to undo it, they must issue a new token.

from apistar.interfaces import Auth
from . import policy

@policy.interactive
def revoke_token(id, auth: Auth):
    token = auth.user.revoke_token(id)

# ## WEB APPLICATION CONFIGURATION

# Users and authentication have a set of related API routes, which will all be
# mounted together. The root of this collection is the submission endpoint for
# creating and updating documents.

from apistar import Route

routes = [
    Route('/', 'GET', whoami),
    Route('/register', 'POST', register_user),
    Route('/token', 'GET', tokens),
    Route('/token', 'POST', issue_token),
    Route('/token/{id}', 'DELETE', revoke_token),
]

# Users also include some helper components, which provide SQL access to user
# storage. These components must be available in the application for the routes
# defined in this chapter to function correctly.

from . import repository as repo

components = repo.components

# Users also include an authentication policy system.

from . import authenticator

authentication = authenticator.all_authenticators

