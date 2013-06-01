from __future__ import absolute_import

import csv
import datetime as dt
import errno
import fcntl
from irc import client as ic
import logging
import os
import os.path
from alicorn import io as aio

log = logging.getLogger(__name__)

class IRCHandler(object):
    def __init__(self, logdir, dateformat='%Y-%m-%d'):
        self.logdir = logdir
        self.dateformat = dateformat
    
    def _logdir(self, source):
        return os.path.join(self.logdir, source)
    
    def make_logdir(self, logdir):
        log.debug("Creating logdir: %s", logdir)
        try:
            os.makedirs(logdir)
        except OSError, e:
            if e.errno == errno.EEXIST:
                log.debug("Logdir %s already exists", logdir)
                return
            raise
    
    def open_logfile(self, logdir, timestamp):
        filename = timestamp.strftime(self.dateformat)
        logpath = os.path.join(logdir, filename)
        log.debug("Opening logfile: %s", logpath)
        return aio.wait_open(logpath, 'a+', fcntl.LOCK_EX)
    
    def log_message(self, event):
        try:
            mask = ic.NickMask(event.source)
            nick = mask.nick
            userhost = mask.userhost
        except IndexError:
            # server-generated events, with no ! or @ in the source
            nick = event.source
            userhost = event.source
        channel = event.target
        message = event.arguments
        timestamp = dt.datetime.utcnow()
        
        logdir = self._logdir(channel)
        self.make_logdir(logdir)
        
        with self.open_logfile(logdir, timestamp) as logfile:
            aio.seek_end(logfile)
            logwriter = csv.writer(logfile)
            logwriter.writerow([timestamp, nick, userhost, event.type] + message)
    
    def convert_private(self, event):
        # pretend the _sender_ is the channel, rather than the recipient.
        try:
            mask = ic.NickMask(event.source)
            nick = mask.nick
        except IndexError:
            nick = event.source
        return ic.Event(
            type=event.type,
            source=event.source,
            target=nick,
            arguments=event.arguments
        )
    
    def on_pubmsg(self, bot, connection, event):
        self.log_message(event)
    
    def on_pubnotice(self, bot, connection, event):
        self.log_message(event)
    
    def on_privmsg(self, bot, connection, event):
        self.log_message(self.convert_private(event))
    
    def on_privnotice(self, bot, connection, event):
        self.log_message(self.convert_private(event))
    
    def on_action(self, bot, connection, event):
        self.log_message(event)
    
    def on_topic(self, bot, connection, event):
        self.log_message(event)
    
    def on_part(self, bot, connection, event):
        self.log_message(event)
    
    def on_kick(self, bot, connection, event):
        self.log_message(event)
    
    def on_join(self, bot, connection, event):
        self.log_message(event)
    
    def on_quit(self, bot, connection, event):
        # quit events do not have channel info tied to them; synth one up for
        # every channel we're logging.
        mask = ic.NickMask(event.source)
        for channel in bot.channels:
            if mask.nick in bot.channels[channel].users():
                synth_event = ic.Event(
                    type=event.type,
                    source=event.source,
                    target=channel,
                    arguments=event.arguments
                )
                self.log_message(synth_event)
    
    def on_nick(self, bot, connection, event):
        # nick events do not have channel info tied to them; synth one up for
        # every channel we're logging.
        mask = ic.NickMask(event.source)
        for channel in bot.channels:
            if mask.nick in bot.channels[channel].users():
                synth_event = ic.Event(
                    type=event.type,
                    source=event.source,
                    target=channel,
                    arguments=[event.target] # here's the new nick
                )
                self.log_message(synth_event)
    