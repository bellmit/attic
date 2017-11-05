command = 'state'

parser_options = dict(
    help='show the state of the game',
    description='Show the state of the game',
)

import dateparser

def configure_parser(parser):
    parser.add_argument(
        '-b', '--before',
        type=dateparser.parse,
        help='Report state as of a point in time (dateparser format), instead of now',
    )

import requests
import yaml
import sys

def run(args):
    resp = requests.get(args.urls.state(before=args.before))
    resp.raise_for_status()
    result = resp.json()

    for error in result['errors']:
        print("cadastre: state error: {0}".format(error), file=sys.stderr)
    del result['errors']

    print(yaml.safe_dump(result, default_flow_style=False))
