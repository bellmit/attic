command = 'revoke-token'

parser_options = dict(
    help='Revoke a Cadastre API token',
    description='Revoke a Cadastre API token'
)

def configure_parser(parser):
    parser.add_argument(
        'id',
        nargs='+',
        help='token IDs to revoke',
    )

from getpass import getpass
import requests
from . import description as d
from . import netrc
from . import whoami

def run(args):
    email = whoami.whoami(args.urls)
    password = getpass()

    for id in args.id:
        request = dict(
            description=d.description(),
        )
        resp = requests.delete(
            url=args.urls.revoke_token(id),
            auth=(email, password),
        )
        resp.raise_for_status()
