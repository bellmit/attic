"""convert annotation events to programs

Revision ID: fd4386ad5439
Revises: d6452513aab5
Create Date: 2017-11-19 00:19:19.732810

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'fd4386ad5439'
down_revision = 'd6452513aab5'
branch_labels = None
depends_on = None


# Use a fixed model here to avoid dealing with shear between Cadastre's model
# (of the current schema, which may be far in the future of this migration) and
# the state as of this migration.

metadata = sa.MetaData()
annotation = sa.Table('annotation', metadata,
    sa.Column('message_id', sa.String, sa.ForeignKey('document.message_id'), primary_key = True),
    sa.Column('revision', sa.Integer, primary_key = True),
    sa.Column('changes', sa.JSON, nullable = False),
    sa.Column('events', sa.JSON, nullable = False),
    sa.Column('program', sa.Text, nullable = False),
)

import decimal as d

from actinide import types as t
from actinide import symbol_table as st

symbols = st.SymbolTable()

def sym(s):
    return t.symbol(s, symbols)

def translate_event(event):
    return t.list(sym('event'), event['office'], event['message'])

def translate_set_key(target, key, val):
    return t.list(sym('object-set'), target, translate_value(key), translate_value(val))

def translate_value(val):
    if isinstance(val, (str, int, bool, type(None))):
        return val
    if isinstance(val, float):
        # Close enough.
        return d.Decimal(val)
    if isinstance(val, list):
        return t.list(sym('vector'), *(translate_value(v) for v in val))
    if isinstance(val, dict):
        return t.list(
            sym('let'),
            t.list(
                t.list(sym('o'), t.list(sym('object'))),
            ),
            *(translate_set_key(sym('o'), k, v) for (k, v) in val.items()),
            sym('o'),
        )

def translate_set(path, value, **kwargs):
    return t.list(sym('set'), path, translate_value(value))

import jsonpointer as jp
import functools

def translate_create(path, **kwargs):
    pointer = jp.JsonPointer(path)
    def generate_create_for_prefix(subpointer, part, pointer):
        return t.list(
            sym('if'),
            t.list(
                sym('not'),
                t.list(sym('list-contains'),
                    t.list(
                        sym('object-keys'),
                        t.list(
                            sym('get'),
                            subpointer.path,
                        ),
                    ),
                    part,
                ),
            ),
            t.list(
                sym('set'),
                pointer.path,
                t.list(sym('object')),
            ),
        )
    def generate_prefixes():
        parts = []
        for part in pointer.parts:
            subpointer = jp.JsonPointer.from_parts(parts)
            parts.append(part)
            part_pointer = jp.JsonPointer.from_parts(parts)
            yield generate_create_for_prefix(subpointer, part, part_pointer)
    return t.list(sym('begin'), *(p for p in generate_prefixes()))

translator = {
    'set': translate_set,
    'create': translate_create,
    # ops "add" and "append" were never, to my knowledge, used
    # op "error" was a catch-all to deal with parse issues, not user-facing
}

def translate_change(change):
    return translator[change['op']](**change)

import json

def generate_comment(changes, events):
    changes = json.dumps(changes)
    events = json.dumps(events)

    return f'''; Translated from the following JSON during a database migration:
; changes: {changes}
; events:  {events}

'''

def translate_annotation(changes, events):
    return '\n'.join([
        generate_comment(changes, events),
        *(t.display(translate_event(event), symbols) for event in events),
        *(t.display(translate_change(change), symbols) for change in changes),
    ])

def upgrade():
    op.add_column('annotation',
        sa.Column('program', sa.Text, nullable = True),
    )
    # Translate existing annotations in Python
    conn = op.get_bind()
    for row in conn.execute(annotation.select()):
        program = translate_annotation(row[annotation.c.changes], row[annotation.c.events])
        conn.execute(
            annotation.update()
                .where(annotation.c.message_id == row[annotation.c.message_id])
                .where(annotation.c.revision == row[annotation.c.revision])
                .values(program=program)
        )
    with op.batch_alter_table('annotation') as batch:
        batch.alter_column('program', nullable = False)
        batch.drop_column('changes')
        batch.drop_column('events')
