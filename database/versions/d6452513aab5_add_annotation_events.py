"""add annotation events

Revision ID: d6452513aab5
Revises: c3d19669c2c4
Create Date: 2017-11-04 23:31:10.716157

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'd6452513aab5'
down_revision = 'c3d19669c2c4'
branch_labels = None
depends_on = None


def upgrade():
    op.add_column('annotation',
        sa.Column('events', sa.JSON, nullable = True),
    )
    op.execute("""
        update annotation set events = '[]'::json
    """)
    op.alter_column('annotation', 'events', nullable = False)
