"""create user schema

Revision ID: 976d3e5450a4
Revises: d79d6a413e52
Create Date: 2017-10-08 16:26:18.804600

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '976d3e5450a4'
down_revision = 'd79d6a413e52'
branch_labels = None
depends_on = None


def upgrade():
    op.create_table(
        'user',
        sa.Column('email', sa.String, primary_key = True),
        sa.Column('password_digest', sa.LargeBinary, nullable = False),
    )

    op.create_table(
        'api_token',
        sa.Column('id', sa.String, primary_key = True),
        sa.Column('email', sa.String, sa.ForeignKey('user.email'), nullable = False),
        sa.Column('token', sa.String, nullable = False),
        sa.Column('description', sa.String, nullable = False),
    )

    op.create_index('api_token_email', 'api_token', ['email'])

    op.add_column('revision',
        sa.Column('submitter', sa.String, sa.ForeignKey('user.email')),
    )
