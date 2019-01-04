from __future__ import absolute_import

from irc import client as ic
from irc import strings as ircstr
import logging

log = logging.getLogger(__name__)

class IRCHandler(object):
    def on_invite(self, bot, connection, event):
        channel, = event.arguments
        connection.join(channel)
