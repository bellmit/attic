command = 'list-documents'

parser_options = dict(
    help='list documents stored in Cadastre',
    description='List documents stored in Cadastre, optionally matching specific criteria'
)

def configure_parser(parser):
    parser.add_argument(
        '-a', '--annotated',
        action='store_true',
        default=None,
        help='include only annotated messages (overrides --no-annotated)',
    )
    parser.add_argument(
        '-A', '--no-annotated',
        action='store_false',
        dest='annotated',
        help='include only non-annotated messages (overrides --annotated)',
    )

import requests
from urllib.parse import urljoin

def run(args):
    resp = requests.get(
        url=args.urls.get_documents(annotated=args.annotated),
    )
    resp.raise_for_status()
    for doc in resp.json():
        print("{doc[message_id]}".format(doc=doc))
