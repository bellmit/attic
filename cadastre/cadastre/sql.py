# All SQLAlchemy models in the registry derive from a common base type, to allow
# them to be used interchangeably within a given transaction.

from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

# To use SQLAlchemy, the following components must be registered.

from apistar.backends import sqlalchemy_backend

components = sqlalchemy_backend.components
