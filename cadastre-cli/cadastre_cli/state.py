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

    for warning in result['warnings']:
        print("cadastre: warning in message {0[message_id]}: {0[message]}".format(warning), file=sys.stderr)

    events = [
        dict(office=e['office'], message=e['message'], timestamp=e['timestamp'])
        for e in result['events']
    ]
    state = result['state']

    print(yaml.safe_dump(dict(events=events, state=state), default_flow_style=False))
