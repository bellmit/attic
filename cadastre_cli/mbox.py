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
#
# This parser does not give a fuck about your Content-Length headers, for
# reasons laid out here: <https://www.jwz.org/doc/content-length.html>. If they
# are present, this code will preserve them verbatim, just like every other byte
# of message content, but this parser makes no effort to understand or reconcile
# any message header, including Content-Length. If that header is present, and
# if it disagrees with the location of the `From ` lines, the `From ` lines will
# be taken as authoritative and the header preserved but disregarded.

def messages(stream):
    # A tiny state machine, driven by lines. Initially, it expects a `From `
    # line, and will reject any other inputs.
    state = ExpectPostmark()
    for line in stream:
        state, message = state.on_line(line)
        if message is not None:
            yield message
    message = state.on_eof()
    if message is not None:
        yield message

# The states.

class ExpectPostmark(object):
    def on_eof(self):
        # Empty mbox stream, return nothing as there is no message.
        return None

    def on_line(self, line):
        if line.startswith(b'From '):
            return Message.new_message(line), None
        raise InvalidMbox()

class Message(object):
    def __init__(self, lines):
        self.lines = lines

    # The accumulated message so far. This is the string sum of the lines, since
    # Python helpfully preserves newlines for us.
    @property
    def message(self):
        return b''.join(self.lines)

    @classmethod
    def make(cls, *args, **kwargs):
        return cls(*args, **kwargs)

    @classmethod
    def new_message(cls, line):
        return cls.make([line])

    def extend_message(self, next_line):
        return self.make(self.lines + [next_line])

    def on_eof(self):
        # End of input. Return the accumulated lines as the body of the final
        # message.
        return self.message

    def on_line(self, line):
        # If the current line is blank, it _may_ be the first line of a
        # postmark. Suspend parsing, but keep the line handy if it turns out to
        # be part of the message body.
        if line == b'\n':
            return MaybePostmark(self.lines, line), None
        # Otherwise, accumulate the line into the current message, and keep
        # parsing.
        return self.extend_message(line), None

class MaybePostmark(object):
    def __init__(self, message_lines, next_line):
        self.message_lines = message_lines
        # Fun fact: self.next_line is always b'\n'. We pretend otherwise because
        # the code's clearer if it's treated uniformly.
        self.next_line = next_line

    @classmethod
    def make(cls, *args, **kwargs):
        return cls(*args, **kwargs)

    def on_eof(self):
        # Wasn't a postmark. EOF means the line was part of the final message.
        return b''.join(self.message_lines + [self.next_line])

    def on_line(self, line):
        # If we're in this state and we see a postmark, then the blank line that
        # got us here was part of that postmark. Throw it out and start
        # processing the next message.
        if line.startswith(b'From '):
            return Message.new_message(line), b''.join(self.message_lines)
        # If we're in this state and we see a blank line, the last blank line
        # that got us here was part of the current message, but the current
        # blank line could still be part of a postmark.
        if line == b'\n':
            return self.make(self.message_lines + [self.next_line], line), None
        # Otherwise, both the current line and the blank line that got us here
        # are part of the current message. Go back to parsing message body.
        return Message(self.message_lines + [self.next_line, line]), None

# ## Errors
#
# If parsing fails, a number of error states are reachable. The following
# exceptions capture them.

# Raised if the parser is unable to reconcile input with the mbox format. This
# is effectively only possible if the first line of input does not begin with
# `From ` - the mbox format is otherwise so flexible that nearly anything is
# valid.
class InvalidMbox(Exception):
    pass
