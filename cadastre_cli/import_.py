command = 'import'

parser_options = dict(
    help='submit mail messages from RFC 2822 files to Cadastre',
    description='Store loose mail messages in Cadastre'
)

import argparse
import sys
from . import mbox

def configure_parser(parser):
    parser.add_argument(
        'file',
        nargs='+',
        metavar='FILE',
        help='path to an email message file (defaults to stdin)',
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
    for file in args.file:
        with file as message:
            resp = requests.post(
                url=args.urls.documents,
                headers={
                    'Content-Type': 'message/rfc822',
                },
                data=message,
                allow_redirects=False,
            )
            resp.raise_for_status()
            if not args.quiet:
                print(args.urls.join(resp.json()['download_url']))
