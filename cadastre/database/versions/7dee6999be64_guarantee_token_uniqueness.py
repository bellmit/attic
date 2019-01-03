"""guarantee token uniqueness

Revision ID: 7dee6999be64
Revises: 976d3e5450a4
Create Date: 2017-10-09 16:28:59.484669

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '7dee6999be64'
down_revision = '976d3e5450a4'
branch_labels = None
depends_on = None

# This is in a separate revision from the migration that creates api_token as
# the missing constraint was not discuvered until _after_ that revision had been
# deployed to staging. Creating a separate revision ensures that the final DB
# includes both, without any need for manual repair on staging.

def upgrade():
    op.create_unique_constraint('api_token_token', 'api_token', ['token'])
