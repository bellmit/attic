import sys
import logging
import importlib as il

log = logging.getLogger(__name__)

def load_handler(handler):
    module_name = handler['module']
    config = handler.get('config', dict())
    log.debug("Loading handler from module %s", module_name)
    module = il.import_module(module_name)
    return module.IRCHandler(**config)
