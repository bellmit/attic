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
        program='; Annotate using Actinide notation.\n'
    )
    if 'current_annotation' in document:
        current_annotation_url = urls.join(document['current_annotation']['download_url'])
        resp = requests.get(current_annotation_url)
        resp.raise_for_status()
        current_annotation = resp.json()

    return current_revision, current_annotation, document['annotate_url']

import os
import subprocess

def run_editor(path):
    editor = os.environ.get('EDITOR', 'vi')
    subprocess.run([editor, path], check=True)

edit_separator = '; --- END ANNOTATIONS (leave this line intact, or remove it and everything after) ---'

def edit_annotation(revision, annotations):
    with tempfile.NamedTemporaryFile(prefix='cadastre-annotation-', mode='w+', encoding='UTF-8') as edit_file:
        print(annotations['program'], file=edit_file)
        print(file=edit_file)
        print(edit_separator, file=edit_file)
        edit_file.write(revision)
        edit_file.flush()
        run_editor(edit_file.name)

        # Can't reuse the existing handle, it has the original state cached
        # somewhere we can't get it. Fortunately, the file has a name at this
        # point, so we can reopen it.
        with open(edit_file.name, 'r', encoding='UTF-8') as edit_file:
            data = edit_file.read()
            separator_at = data.find('\n' + edit_separator + '\n')
            if separator_at >= 0:
                data = data[:separator_at]
            if data.strip() == '':
                return None
            return dict(program=data)

def update_annotation(annotate_url, annotation):
    resp = requests.post(annotate_url, json=annotation)
    resp.raise_for_status()


def run(args):
    for message in args.message:
        raw_revision, annotation, annotate_url = fetch_document(args.urls, message)
        annotation = edit_annotation(raw_revision, annotation)
        if annotation is None:
            print(f"Empty annotation for {message}, skipping")
        else:
            update_annotation(args.urls.join(annotate_url), annotation)
