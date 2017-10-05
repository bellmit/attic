command = 'fix-postmarks'

parser_options = dict(
    help='Attempt to repair mbox files with damaged postmarks',
    description='''
        Attempt to repair mbox files with damaged postmarks. A postmark is
        damaged if it does not contain an "@" character - this command assumes
        that such a postmark was intended to be part of the preceding message,
        and >From-mangles it in the output.
    ''',
)

import argparse
import sys
from . import mbox

def configure_parser(parser):
    parser.add_argument(
        '-i', '--input',
        metavar='FILE',
        help='path to an email message file to read from (defaults to stdin)',
        default=sys.stdin.buffer,
        type=argparse.FileType('rb'),
    )
    parser.add_argument(
        '-o', '--output',
        metavar='FILE',
        help='path to an email message file to write to (defaults to stdout)',
        default=sys.stdout.buffer,
        type=argparse.FileType('wb'),
    )

import email
import email.generator as eg
import email.parser as ep

def run(args):
    first_message = True
    with args.input as input, args.output as output:
        for postmark, body in mbox.messages(args.input):
            if not first_message:
                output.write(b'\n')
            if b'@' not in postmark:
                output.write(b'>')
            output.write(postmark)
            output.write(body)
            first_message = False
