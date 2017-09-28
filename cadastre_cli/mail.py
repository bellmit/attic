parser_options = dict(
    help='submit mail messages to Cadastre',
    description='Store a mail message in Cadastre'
)

import argparse
import sys
from . import mbox

def configure_parser(parser):
    parser.add_argument(
        'file',
        nargs='?',
        metavar='FILE',
        help='path to an email message file (defaults to stdin)', # XXX this should say "mbox" and also parse mbox
        default=sys.stdin,
        type=argparse.FileType('rb'),
    )

import requests
from urllib.parse import urljoin

def run(args):
    document_url = urljoin(args.cadastre_url, 'document/')
    with args.file as file:
        for message in mbox.messages(file):
            resp = requests.post(
                url=document_url,
                headers={
                    'Content-Type': 'message/rfc822',
                },
                data=message,
                allow_redirects=False,
            )
            resp.raise_for_status()
            print(urljoin(args.cadastre_url, resp.json()['download_url']))
