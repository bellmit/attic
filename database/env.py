# Stuff the current working directory into Alembic's python module search path
# so that the configuration process can find the `cadastre` package. We don't
# rely on installing `cadastre` in the current python env's site-packages
# directory, and Alembic removes the cwd from the search path by default.

import sys

sys.path.insert(0, '')

# Find the database, reusing the `cadastre.config` mechanism for picking a
# database.

from cadastre.config import env

database_url = env['DATABASE_URL']

# Run Alembic.

from alembic import context
from sqlalchemy import create_engine

def run_migrations_offline():
    """Run migrations in 'offline' mode.

    This configures the context with just a URL
    and not an Engine, though an Engine is acceptable
    here as well.  By skipping the Engine creation
    we don't even need a DBAPI to be available.

    Calls to context.execute() here emit the given string to the
    script output.

    """
    context.configure(url=database_url, literal_binds=True)

    with context.begin_transaction():
        context.run_migrations()


def run_migrations_online():
    """Run migrations in 'online' mode.

    In this scenario we need to create an Engine
    and associate a connection with the context.

    """
    connectable = create_engine(database_url)

    with connectable.connect() as connection:
        context.configure(connection=connection)

        with context.begin_transaction():
            context.run_migrations()

if context.is_offline_mode():
    run_migrations_offline()
else:
    run_migrations_online()
