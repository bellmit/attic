command = 'whoami'

parser_options = dict(
    help='Validate current API credentials',
    description='Prints the login email associated with the current account'
)

import argparse
import sys
from . import mbox

def configure_parser(parser):
    pass

import requests
from . import description as d
from . import netrc

def whoami(urls):
    resp = requests.get(
        url=urls.user,
    )
    resp.raise_for_status()
    user = resp.json()
    return user['email']

def run(args):
    print(whoami(args.urls))
