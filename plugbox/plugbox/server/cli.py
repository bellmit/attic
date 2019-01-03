import argparse as ap
import os

from plugbox.server import service as s
from plugbox.server.commands import init, list_versions, next_version, remove_version, rollback_to, upgrade_to

def parse_args():
    parser = ap.ArgumentParser()

    parser.add_argument(
        '--basedir', '-d',
        default=os.environ.get('HOME', '.'),
        type=s.Service,
        dest='service',
        help='Service base directory (default: %(default)s)',
    )

    commands = parser.add_subparsers(
        description='%(prog)s commands (for per-command help, run: %(prog)s COMMAND --help)',
        metavar='COMMAND',
    )

    init.register(commands)
    list_versions.register(commands)
    next_version.register(commands)
    upgrade_to.register(commands)
    rollback_to.register(commands)
    remove_version.register(commands)

    return parser.parse_args()

def main():
    args = parse_args()
    return args.command_main(args)
