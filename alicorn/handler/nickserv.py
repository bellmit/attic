from __future__ import absolute_import

from irc import client as ic
from irc import strings as ircstr
import logging

log = logging.getLogger(__name__)

class IRCHandler(object):
    def __init__(self, identity=None, password=None, nickserv='NickServ'):
        self.identity = identity
        self.password = password
        self.nickserv = ircstr.lower(nickserv)
    
    def on_welcome(self, bot, connection, event):
        self.identify(connection)
    
    def identify(self, connection):
        credentials = ['IDENTIFY']
        if self.identity is not None:
            credentials.append(self.identity)
        if self.password is not None:
            credentials.append(self.password)
        connection.privmsg(self.nickserv, ' '.join(credentials))
