# A streaming mbox parser.
#
# Cadastre deals with inputs delivered by MTAs such as postfix, and MDAs such as
# Procmail. These systems operate by delivering a stream on stdin consisting of
# a `From ` line followed by an email message.
#
# This is, effectively, identical to a one-item mbox. However, many mbox
# parsers, including Python's `mailbox.mbox` parser, assume that the underlying
# store is a file, and therefore that they can seek(1) within the file to find
# specific messages. This assumption does not hold if the mbox stream is
# delivered via pipe to stdin, as happens in a mail-delivery environment.
#
# mbox files, in this model, are broadly line oriented. Python's iteration
# interface helps us here: iterating over a file-like object yields lines, with
# trailing newlines intact. This means that joining the lines of a subset of a
# file-like object exactly reproduces the original bytes of that object, and
# therefore that we can exactly reproduce the part of a stream corresponding to
# the message in an mbox stream.
#
# This parser makes no effort to understand the structure of individual
# messages. That step can be handled using the `email.parser` module, or using
# any other RFC-2822-parsing code.

def messages(stream):
    # A tiny state machine, driven by lines. Initially, it expects a `From `
    # line, and will reject any other inputs.
    state = ExpectFrom()
    for line in stream:
        state, message = state.on_line(line)
        if message is not None:
            yield message
    message = state.on_eof()
    if message is not None:
        yield message

# The states.

#
class ExpectFrom(object):
    def on_eof(self):
        # Empty mbox stream, return nothing as there is no message.
        return None

    def on_line(self, line):
        if line.startswith(b'From '):
            return Message([]), None
        raise InvalidMbox()

class Message(object):
    def __init__(self, lines):
        self.lines = lines

    # The accumulated message so far. This is the string sum of the lines, since
    # Python helpfully preserves newlines for us.
    @property
    def message(self):
        return b''.join(self.lines)

    def on_eof(self):
        # End of input. Return the accumulated lines as the body of the final
        # message.
        return self.message

    def on_line(self, line):
        # If the current line is a From line, it marks the end of the current
        # message and the beginning of the following message.
        if line.startswith(b'From '):
            return type(self)([]), self.message
        # Otherwise, accumulate it into the current message, and keep parsing.
        return type(self)(self.lines + [line]), None

# ## Errors
#
# If parsing fails, a number of error states are reachable. The following
# exceptions capture them.

# Raised if the parser is unable to reconcile input with the mbox format.
class InvalidMbox(Exception):
    pass
