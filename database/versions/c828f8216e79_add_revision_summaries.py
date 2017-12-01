"""add revision summaries

Revision ID: c828f8216e79
Revises: fd4386ad5439
Create Date: 2017-11-30 22:21:30.617879

"""
from alembic import op
import sqlalchemy as sa

from cadastre.document import metadata as cdm

# revision identifiers, used by Alembic.
revision = 'c828f8216e79'
down_revision = 'fd4386ad5439'
branch_labels = None
depends_on = None

metadata = sa.MetaData()
revision_ = sa.Table('revision', metadata,
    sa.Column('message_id', sa.String, sa.ForeignKey('document.message_id'), primary_key = True),
    sa.Column('revision', sa.Integer, primary_key = True),
    sa.Column('body', sa.LargeBinary, nullable = False),
    sa.Column('content_type', sa.String, nullable = False),
    sa.Column('subject', sa.String),
)

def detect_subject(body, content_type):
    extractor = cdm.extractor_for_mime_type(content_type)
    metadata = extractor(body)
    return metadata.subject if metadata.subject else '(No subject)'

def upgrade():
    op.add_column('revision',
        sa.Column('subject', sa.String, nullable = True),
    )

    conn = op.get_bind()
    for row in conn.execute(revision_.select()):
        subject = detect_subject(row[revision_.c.body], row[revision_.c.content_type])
        conn.execute(
            revision_.update()
                .where(revision_.c.message_id == row[revision_.c.message_id])
                .where(revision_.c.revision == row[revision_.c.revision])
                .values(subject=subject)
        )

    op.alter_column('revision', 'subject', nullable = False)
