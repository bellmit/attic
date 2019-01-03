# This service consumes configuration via environment variables only.
#
# ## SETTING CONFIGURATION KEYS
#
# Every configurable value that controls the registry's operation is controlled
# using an environment variable of the same name.

from apistar import environment, typesystem

class Env(environment.Environment):
    properties = {
        'DATABASE_URL': typesystem.string(default='postgresql://localhost/cadastre-devel')
    }

# ## DATABASE
#
# Config keys:
# * `DATABASE_URL`
#
# The registry relies on a single PostgreSQL database to store its data. The
# `DATABASE_URL` configuration key  holds the URL of a PostgreSQL database used
# to hold the registry's data.
#
# On Heroku, this is automatically provided by the `heroku-postgresql` Add-On.
# On other platforms, and locally, this environment variable must be exported by
# hand. It has the format
# `postgresql://<username>:<password>@<host>[:<port>]/<database>`, or any other
# format supported by libpq. If this key is not set, the repository will assume
# `postgresql://localhost/cadastre-devel`.
#
# The schema in this database is controlled by Alembic migrations. To run the
# migrations, run the `bin/release` script.

from . import sql
from . import authn

env = Env()

settings = {
    'AUTHENTICATION': authn.authentication,
    'DATABASE': {
        'URL': env['DATABASE_URL'],
        'METADATA': sql.Base.metadata,
    },
    'STATICS': {
        'ROOT_DIR': 'static',
        'PACKAGE_DIRS': ['apistar'],
    },
    'TEMPLATES': {
        'ROOT_DIR': 'static',
        'PACKAGE_DIRS': ['apistar'],
    },
}
