# Cadastre CLI is structured as a suite of "subcommands" - verbs that act like
# top-level CLI commaands. Each command provides an argparse subparser, which
# allows us to regularize command-line argument processing and provides some
# helpful facilities for working with streams.

# The top-level parser also handles defaults, including the location of the
# Cadastre service. Individual commands must attach subparsers to this parser to
# work with the CLI parsing setup defined here.

import argparse
import os
from . import urls

def make_toplevel_parser():
    parser = argparse.ArgumentParser(
        description="A command-line tool for working with Cadastre documents",
    )
    parser.add_argument(
        '--cadastre-url',
        nargs='?',
        metavar='URL',
        dest='urls',
        default=os.environ.get('CADASTRE_URL', 'https://cadastre.herokuapp.com/'),
        help="Cadastre server URL (overrides CADASTRE_URL from environment)",
        type=urls.Urls,
    )
    return parser

# Run through a fixed list of commands and attach them to an existing parser.
# Each command will automatically populate the `run` namespace key in the final
# parse result with a function that can be invoked to run that command's entry
# point.
#
# Each subcommand is exported by a module (or, really, any object) with a
# three-part API:
#
# * The `command` property will be used as the command name. This SHOULD be the
#   name of the module with underscores replaced with dashes, but CAN be any
#   appropriate, unique string.
#
# * The `parser_options` dict will be used to pass arguments to the Argparse
#   parser. This can be used to allow the module to provide top-level help and
#   other metadata.
#
# * The `configure_parser` function will be called to allow the command to
#   register CLI arguments and options with the parser.
#
# * The `run` function will be used to invoke the command, and will receive the
#   parsed argparse Namespace as an argument. This should return something
#   truthy or integer-shaped, so that the result can be used as an exit status.

from . import register
from . import login
from . import whoami
from . import tokens
from . import revoke_token
from . import mail
from . import fix_postmarks

def attach_commands(parser):
    subparsers = parser.add_subparsers(
        title="subcommands",
    )

    for module in [register, login, whoami, tokens, revoke_token, mail, fix_postmarks]:
        mod_parser = subparsers.add_parser(module.command, **module.parser_options)
        mod_parser.set_defaults(run=module.run)
        module.configure_parser(mod_parser)


# Rig up commands to a unified parser and apply it to the CLI args from
# `sys.argv`. The returned Namespace is compatible with the `run` function,
# below, to make it easy to parse and invoke a Cadastre CLI subcommand.

def parse_args():
    parser = make_toplevel_parser()
    attach_commands(parser)
    return parser.parse_args()

# Execute the `run` property (a function) in the passed Namespace and return its
# result. The run function will receive the namespace as an argument, to give it
# access to CLI options and arguments.

def run(args):
    return args.run(args)
