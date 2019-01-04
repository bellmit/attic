import irc.client as ic
import functools as f
import logging
import re

log = logging.getLogger(__name__)

def is_self(bot, nick):
	"""Returns true if the speaker is the bot."""
	return nick == bot._nickname

words = re.compile(r' ')

def parse_line(line):
	parts = re.split(words, line)
	if len(parts) > 0 and parts[0].endswith(':'):
		addressee = parts[0][:-1]
		line = parts[1:]
	else:
		addressee = None
		line = parts
	return addressee, line

class Reply(object):
	def __init__(self, speaker, venue, connection):
		self.speaker = speaker
		self.venue = venue
		self.connection = connection

	def __call__(self, message):
		self.connection.privmsg(self.venue, "%s: %s" % (self.speaker, message))

class PrivateReply(object):
	def __init__(self, recipient, connection):
		self.recipient = recipient
		self.connection = connection

	def __call__(self, message):
		self.connection.privmsg(self.recipient, message)

def responder(method):
	"""Picks apart chat lines of the form

		addressee: message

	and

		message

	and determines the addressee, speaker, and venue, then composes an
	appropriate callback for responding to the message if appropriate. The
	decorated method's signature is changed appropriately too, to turn a 
	conversational responder ``(self, bot, speaker, addressee, message, reply)`` into
	a normal IRC callback ``(self, bot, connection, event)``.
	"""
	@f.wraps(method)
	def wrapper(self, bot, connection, event):
		line = ''.join(event.arguments)
		speaker = event.source
		addressee, message = parse_line(line)

		public = ic.is_channel(event.target)
		to_self = is_self(bot, addressee) or not public

		if public and to_self:
			reply = Reply(speaker.nick, event.target, connection)
		elif public and not to_self:
			reply = Reply(addressee, event.target, connection)
		elif not public:
			addressee = bot._nickname
			reply = PrivateReply(event.source.nick, connection)
		else:
			log.warn("Couldn't determine speaker for event: %r", event)

		return method(self, bot, speaker, addressee, message, reply)
	return wrapper
