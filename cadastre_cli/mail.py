command = 'mail'

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
        default=sys.stdin.buffer,
        type=argparse.FileType('rb'),
    )
    parser.add_argument(
        '-q', '--quiet',
        action='store_true',
        help='do not print message URLs to stdout',
    )

import requests
from urllib.parse import urljoin

def run(args):
    with args.file as file:
        for message in mbox.messages(file):
            resp = requests.post(
                url=args.urls.documents,
                headers={
                    'Content-Type': 'message/rfc822',
                },
                data=message.body,
                allow_redirects=False,
            )
            resp.raise_for_status()
            if not args.quiet:
                print(args.urls.join(resp.json()['download_url']))
