"""Create document and revision schema

Revision ID: d79d6a413e52
Revises:
Create Date: 2017-09-15 22:34:13.032587

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'd79d6a413e52'
down_revision = None
branch_labels = None
depends_on = None


def upgrade():
    op.create_table(
        'revision',
        sa.Column('message_id', sa.String, primary_key = True),
        sa.Column('revision', sa.Integer, primary_key = True),
        sa.Column('date', sa.DateTime, nullable = False),
        sa.Column('body', sa.LargeBinary, nullable = False),
        sa.Column('content_type', sa.String, nullable = False),
    )

    op.create_table(
        'document',
        sa.Column('message_id', sa.String, primary_key = True),
        sa.Column('current_revision', sa.Integer, nullable = False),
        sa.ForeignKeyConstraint(
            ['message_id', 'current_revision'],
            ['revision.message_id', 'revision.revision'],
            deferrable = True,
            initially = 'DEFERRED',
        ),
    )

    op.create_foreign_key(
        'revision_message_id_fkey',
        'revision',
        'document',
        ['message_id'],
        ['message_id'],
        deferrable = True,
        initially = 'DEFERRED',
    )


def downgrade():
    op.drop_table('document')
    op.drop_table('revision')
