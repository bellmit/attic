def register(parser):
    upgrade_to = parser.add_parser('upgrade-to', help='Upgrades to the next version, checking that it matches the expected version.')
    upgrade_to.add_argument(
        'version',
        help='expected next release name'
    )
    upgrade_to.set_defaults(command_main=run)

def run(args):
    service = args.service
    version = service.upgrade_to(args.version)
