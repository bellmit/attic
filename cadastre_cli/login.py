command = 'login'

parser_options = dict(
    help='Log into a cadastre registry',
    description='Log into a registry with an existing account'
)

def configure_parser(parser):
    parser.add_argument(
        'email',
        help='email address to log in',
    )

from getpass import getpass
import requests
from . import description as d
from . import netrc

def run(args):
    password = getpass()

    request = dict(
        description=d.description(),
    )
    resp = requests.post(
        url=args.urls.issue_token,
        json=request,
        auth=(args.email, password),
    )
    resp.raise_for_status()
    token = resp.json()

    netrc.update_netrc(args.urls.host, args.email, token['token'])
