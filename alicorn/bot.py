import logging

from irc import bot

log = logging.getLogger(__name__)

def do_nothing(bot, connection, event):
    pass

class AlicornBot(bot.SingleServerIRCBot):
    def __init__(self, channels, handlers, *args, **kwargs):
        super(AlicornBot, self).__init__(*args, **kwargs)
        self.default_channels = channels
        self.handlers = handlers
        self.ircobj.add_global_handler("all_events", self._dispatch_handlers, -30)
    
    def _dispatch_handlers(self, conn, event):
        log.debug("Dispatching event to handlers: %s", event.type)
        
        for handler in self.handlers:
            method = getattr(handler, "on_%s" % event.type, do_nothing)
            if method != do_nothing:
                log.debug("Invoking handler %s", method)
            try:
                response = method(self, conn, event)
                if response is not None:
                    log.debug("Handler %s consumed message", method)
                    return response
            except KeyboardInterrupt:
                raise
            except:
                log.exception("Unhandled exception in handler %s, ignoring", method)
                pass
    
    def on_welcome(self, conn, event):
        for channel in self.default_channels:
            conn.join(channel)
