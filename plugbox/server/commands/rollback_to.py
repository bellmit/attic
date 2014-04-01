def register(parser):
    rollback_to = parser.add_parser('rollback-to', help='Downgrades to the previous version, checking that it matches the expected version.')
    rollback_to.add_argument(
        'version',
        help='expected previous release name'
    )
    rollback_to.set_defaults(command_main=run)

def run(args):
    service = args.service
    version = service.rollback_to(args.version)
