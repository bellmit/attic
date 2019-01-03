def register(parser):
    list_versions = parser.add_parser('list-versions', help='Prints all available versions on stdout with annotations.')
    list_versions.set_defaults(command_main=run)

def run(args):
    service = args.service
    versions = service.versions()
    live_version = service.live
    next_version = service.next
    prev_version = service.previous
    for version in versions:
        state = ""
        if version == live_version:
            state = "live"
        elif version == next_version:
            state = "next"
        elif version == prev_version:
            state = "prev"
        print("{state}\t{version.name}".format(**locals()))
