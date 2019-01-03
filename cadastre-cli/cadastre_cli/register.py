command = 'register'

parser_options = dict(
    help='Register with a cadastre registry',
    description='Create an account on a cadastre registry'
)

def configure_parser(parser):
    parser.add_argument(
        'email',
        help='email address to register',
    )

from getpass import getpass
import requests
from . import description as d
from . import netrc

def run(args):
    password = getpass()
    confirm_password = getpass(prompt="Confirm password: ")

    if password != confirm_password:
        print("Passwords did not match.")
        return 1

    request = dict(
        email=args.email,
        password=password,
        token_description=d.description(),
    )
    resp = requests.post(
        url=args.urls.register_user,
        json=request,
    )
    resp.raise_for_status()
    token = resp.json()

    netrc.update_netrc(args.urls.host, args.email, token['token'])
