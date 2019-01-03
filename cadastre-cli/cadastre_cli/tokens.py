command = 'tokens'

parser_options = dict(
    help='List active API tokens',
    description='List active API tokens on a registry'
)

import argparse
import sys
from . import mbox

def configure_parser(parser):
    pass

from getpass import getpass
import requests
from . import description as d
from . import netrc
from . import whoami

def run(args):
    email = whoami.whoami(args.urls)
    password = getpass()

    resp = requests.get(
        url=args.urls.tokens,
        auth=(email, password),
    )
    resp.raise_for_status()
    tokens = resp.json()

    fmt = "{id:36}  {description:36}"
    print(fmt.format(id="ID", description="Description"))
    for token in tokens:
        print(fmt.format(**token))
