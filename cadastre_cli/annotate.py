command = 'annotate'

parser_options = dict(
    help='annotate or re-annotate a document',
    description='Annotate or re-annotate a document',
)

def configure_parser(parser):
    parser.add_argument(
        'message',
        nargs='+',
        help='message-id of the document(s) to annotate',
    )

import requests
import frontmatter
import tempfile

def fetch_document(urls, message_id):
    resp = requests.get(urls.document(message_id))
    resp.raise_for_status()
    document = resp.json()

    current_revision_url = urls.join(document['current_revision']['download_url'])
    resp = requests.get(current_revision_url)
    resp.raise_for_status()
    current_revision = resp.text

    current_annotation = dict(
        events=[
            dict(
                office='some-office',
                message='Your summary here (repeat as needed)',
            ),
        ],
        changes=[
            dict(
                op='create',
                path='/example/path',
            ),
            dict(
                op='set',
                path='/example/path/value',
                value='Hello, world',
            ),
        ],
    )
    if 'current_annotation' in document:
        current_annotation_url = urls.join(document['current_annotation']['download_url'])
        resp = requests.get(current_annotation_url)
        resp.raise_for_status()
        current_annotation = resp.json()

    return frontmatter.Post(current_revision, **current_annotation), document['annotate_url']

import os
import subprocess

def run_editor(path):
    editor = os.environ.get('EDITOR', 'vi')
    subprocess.run([editor, path], check=True)

def edit_annotations(document):
    with tempfile.NamedTemporaryFile() as edit_file:
        frontmatter.dump(document, edit_file, handler=frontmatter.YAMLHandler())
        edit_file.flush()
        run_editor(edit_file.name)

        # Can't reuse the existing handle, it has the original state cached
        # somewhere we can't get it. Fortunately, the file has a name at this
        # point, so we can reopen it.
        with open(edit_file.name, 'rb') as edit_file:
            return frontmatter.load(edit_file, handler=frontmatter.YAMLHandler())

def update_annotations(annotate_url, annotations):
    resp = requests.post(annotate_url, json=annotations)
    resp.raise_for_status()


def run(args):
    for message in args.message:
        document, annotate_url = fetch_document(args.urls, message)
        document = edit_annotations(document)
        if document.metadata == dict() and document.content == '':
            print(f"Empty metadata file for {message}, skipping")
        else:
            update_annotations(args.urls.join(annotate_url), document.metadata)
