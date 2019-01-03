def register(parser):
    remove_version = parser.add_parser('remove-version', help='Removes versions.')
    remove_targets = remove_version.add_mutually_exclusive_group(required=True)
    remove_targets.add_argument(
        '--version', '-v',
        metavar='VERSION',
        nargs='+',
        dest='versions',
        help="Specific version[s] to remove"
    )
    remove_targets.add_argument(
        '--previous', '-p',
        default=False,
        action='store_true',
        help="Remove previous version",
    )
    remove_targets.add_argument(
        '--next', '-n',
        default=False,
        action='store_true',
        help="Remove next version",
    )
    remove_targets.add_argument(
        '--purge', '-P',
        default=False,
        action='store_true',
        help="Remove all but live, previous, and next versions",
    )
    remove_version.set_defaults(command_main=run)

def run(args):
    service = args.service
    if args.versions is not None:
        for version in args.versions:
            service.remove_version(version)
    if args.previous:
        service.remove_previous()
    if args.next:
        service.remove_next()
    if args.purge:
        service.purge()
