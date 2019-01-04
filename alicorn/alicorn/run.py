from __future__ import absolute_import

import argparse as ap
import logging
import yaml

from irc import bot
from alicorn import bot as ab
from alicorn import handlers as ah

log = logging.getLogger(__name__)

def parse_args():
    parser = ap.ArgumentParser(description='An IRC bot.')
    parser.add_argument(
        '-c', '--config',
        metavar='FILE',
        default='alicorn.conf',
        help='Configuration file to run (default: %(default)s)',
    )
    parser.add_argument(
        '-D', '--debug',
        action='store_const',
        default=False,
        const=True,
        help='Configuration file to run (default: %(default)s)',
    )
    return parser.parse_args()

def init_logging(args):
    level = logging.INFO
    if args.debug:
        level = logging.DEBUG
    logging.basicConfig(level=level)

def load_config(name):
    with open(name) as config:
        return yaml.safe_load(config)

def main():
    args = parse_args()
    init_logging(args)
    config = load_config(args.config)
    
    handlers = [
        ah.load_handler(handler)
        for handler in config['handlers']
    ]
    
    servers = [bot.ServerSpec(**server) for server in config['servers']]
    client = ab.AlicornBot(config['channels'], handlers, servers, **config['connection'])
    client.start()

if __name__ == '__main__':
    import sys
    sys.exit(main())
