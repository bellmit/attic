import datetime as dt

def iso_now():
    now = dt.datetime.now()
    now = now.replace(microsecond=0) # don't run two deploys in the same second.
    return now.isoformat()

def register(parser):
    new_version = parser.add_parser('next-version', help='creates a new version within a service following the deployed version')
    new_version.add_argument(
        'name',
        nargs='?',
        default=iso_now(),
        help='release name (default: an ISO 8601 timestamp such as %(default)s)'
    )
    new_version.add_argument(
        '--clone', '-c',
        default=False,
        action='store_true',
        help='clone the live version when creating a new version'
    )
    new_version.set_defaults(command_main=run)

def run(args):
    service = args.service
    if args.clone:
        version = service.clone_next_version(args.name)
    else:
        version = service.create_next_version(args.name)
    print("{version.name}".format(**locals()))
