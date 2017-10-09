# Storage for Cadastre authentication data records information necessary to
# identify the user originating any given request. This stores three key facts
# about each user:
#
# * Their login email address. This isn't actually validated - we don't send it
#   mail to confirm it - but given the closed audience I don't expect that to be
#   a source of trouble.
#
# * A bcrypt digest of their password. This is the user's primary credential. We
#   assume that changing a password frequently and using unique passwords per
#   site are both good ideas, but also that users will not actually do those
#   things reliably. To protect users from these risks, passwords are stored
#   digested so that a database dump (or a rogue administrator) cannot recover
#   passwords for any practical amount of effort.
#
# * A set - possibly empty - of generated API tokens that are currently valid
#   for the user. API tokens are credentials, but they're much more ephemeral
#   than passwords. As we must be able to identify a user by token alone,
#   they're stored in the clear; a database dump will contain all valid tokens
#   at that point in time.
#
# In the event of a data disclosure event, such as a public database dump, it's
# trivial to invalidate all existing tokens (by deleting them from the
# underlying database) and to advise users to issue themselves new tokens using
# their passwords. Because tokens are always generated on the service's side,
# there are no meaningful risks that a user will have the same token on Cadastre
# and on another service.

# ## REPOSITORY

# A repository handles storage and retrieval of users against an underlying data
# store. A `Repository` wraps a specific SQLAlchemy Session, and should not
# outlive it.

import apistar.exceptions
import bcrypt
import sqlalchemy.exc

# For development convenience, this directly converts to an HTTP response.
class UserExistsError(apistar.exceptions.ValidationError):
    pass

class Repository(object):
    def __init__(self, session):
        self.session = session

    # Submits a new user to this repository.
    #
    # In either case, the user model object used to represent the user will be
    # returned.
    #
    # If the user already exists, this method raises a UserExistsError.
    def create_user(self, email, password):
        password_digest = digest_password(password)
        user = User(
            email=email,
            password_digest=password_digest,
        )
        try:
            self.session.add(user)
            self.session.flush([user]) # Ensure we raise an exception immediately if the user exists.
            return user
        except sqlalchemy.exc.IntegrityError as e:
            raise UserExistsError from e

    # Find an existing user by email address. Used to support username
    # authentication and as an entrypoint into token management. Returns None if
    # no user exists with the requested email.
    def find_user(self, email):
        return self.session.query(User).filter(User.email == email).one_or_none()

    # Find an existing user by API token. Used to support token authentication.
    # Returns None if no user exists with the requested token.
    def find_token_user(self, token):
        return self.session.query(User).join(ApiToken).filter(ApiToken.token == token).one_or_none()

# Encodes and digests a password.
def digest_password(password):
    password = password.encode('UTF-8')
    password_salt = bcrypt.gensalt()
    password_digest = bcrypt.hashpw(password, password_salt)
    return password_digest

# ## MODELS

# A User holds the email address and password credential for a user.

import bcrypt
from cadastre import sql
from sqlalchemy import Column, String, LargeBinary
from sqlalchemy.orm import relationship, backref, Session
import uuid

class User(sql.Base):
    __tablename__ = 'user'
    email = Column(String, primary_key = True)
    password_digest = Column(LargeBinary, nullable = False)

    tokens = relationship('ApiToken',
        backref=backref('user'),
        collection_class=set,
    )

    # Checks a password against the stored password.
    def validate_password(self, candidate):
        candidate = candidate.encode('UTF-8')
        return bcrypt.checkpw(candidate, self.password_digest)

    # Change the stored password for this user.
    def update_password(self, password):
        self.password_digest = digest_password(password)

    # Generate and store a new API token for this user. Returns the token.
    def generate_token(self, description):
        id = uuid.uuid4()
        token = uuid.uuid4()
        stored_token = ApiToken(
            id = id,
            email = self.email,
            token = token,
            description = description,
        )
        self.tokens.add(stored_token)
        return stored_token

    # Revoke a token by deleting it from the underlying data store
    def revoke_token(self, id):
        # I tried implementing this as operations on self.tokens, but SQLAlchemy
        # tried to translate my code by unsetting ApiToken.email (which isn't
        # correct, or what I wanted). This is kind of ugly, but it dispatches
        # the right delete query.
        session = Session.object_session(self)
        # The email join is technically redundant since id is a primary key, but
        # it never hurts to be specific about the join condition.
        session.\
            query(ApiToken).\
            filter(ApiToken.email == self.email).\
            filter(ApiToken.id == id).\
            delete()

# An ApiToken holds a bearer credential for a user. A user may have multiple
# bearer credentials.

from cadastre import sql

from sqlalchemy import Column, String, ForeignKey

class ApiToken(sql.Base):
    __tablename__ = 'api_token'
    id = Column(String, primary_key = True)
    email = Column(String,
        ForeignKey('user.email'),
        nullable = False,
    )
    token = Column(String, unique = True, nullable = False)
    description = Column(String, nullable = False)


# ## COMPONENTS

# The repository for a request can be made available as a component, relying on
# the SQLAlchemy session component for the same request.

from apistar.backends.sqlalchemy_backend import Session

def request_repository(session: Session):
    return Repository(session)

from apistar import Component

components = [
    Component(Repository, init = request_repository, preload = False),
]
