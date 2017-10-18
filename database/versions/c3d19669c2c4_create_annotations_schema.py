"""create annotations schema

Revision ID: c3d19669c2c4
Revises: 7dee6999be64
Create Date: 2017-10-09 19:40:49.685992

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'c3d19669c2c4'
down_revision = '7dee6999be64'
branch_labels = None
depends_on = None


def upgrade():
    op.create_table(
        'annotation',
        sa.Column('message_id', sa.String, sa.ForeignKey('document.message_id'), primary_key = True),
        sa.Column('revision', sa.Integer, primary_key = True),
        sa.Column('changes', sa.JSON, nullable = False),
        sa.Column('submitter', sa.String, sa.ForeignKey('user.email')),
    )

    op.add_column('document',
        sa.Column('current_annotation', sa.Integer),
    )

    op.create_foreign_key(
        'document_current_annotation',
        'document',
        'annotation',
        ['message_id', 'current_annotation'],
        ['message_id', 'revision'],
    )
