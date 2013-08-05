from __future__ import absolute_import

import logging
import requests as r

from alicorn import conversation as ac

log = logging.getLogger(__name__)

class IRCHandler(object):
    def __init__(self, environments):
        self.environments = environments
    
    @ac.responder
    def on_pubmsg(self, bot, speaker, addressee, message, reply):
        if addressee != bot._nickname:
            log.debug("Ignoring: addressee %s is not me (%s)", addressee, bot._nickname)
            return
        if len(message) != 3:
            log.debug("Ignoring: wrong number of parts: %r", message)
            return
        if message[0] != 'version' or message[1] != 'in':
            log.debug("Ignoring: not my keywords: %r", message)
            return
        environment = message[2]
        if environment not in self.environments:
            reply("I don't know where that is.")
            return

        url = self.environments[environment]['url']
        version_response = r.head(url, allow_redirects=True)
        version = version_response.headers.get('X-Version', 'unknown')
        git_version = version_response.headers.get('X-Git-Version', 'unknown')

        reply("Version in %s is %s (git version %s)" % (environment, version, git_version))

    on_privmsg = on_pubmsg