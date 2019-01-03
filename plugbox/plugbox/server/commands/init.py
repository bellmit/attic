def register(parser):
    init = parser.add_parser('init', help='creates a new service directory tree')
    init.set_defaults(command_main=run)

def run(args):
    service = args.service
    service.init()
