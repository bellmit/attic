import os
import os.path
import ConfigParser as configparser
from bugcli import path

class Configuration(object):
    def __init__(self, config_files):
        self.config_files = config_files
        self.args = None
    
    def apply_arguments(self, args):
        self.args = args
    
    def get_arg(self, name):
        if self.args is None:
            return None
        return getattr(self.args, name, None)
    
    def get(self, section, name, default=None):
        arg = self.get_arg(name)
        if arg is not None:
            return arg
        if self.config_files.has_option(section, name):
            return self.config_files.get(section, name)
        return default
    
    def require(self, section, name):
        value = self.get(section, name)
        if value == None:
            raise ValueError("Required option %r not provided." % (name,))
        return value

def enumerate_config_files():
    project_config = path.first_ancestor_with(os.getcwd(), '.bugtracker')
    if project_config is not None:
        yield project_config
    yield os.path.expanduser('~/.bug-cli.conf')

def load():
    config_files = configparser.ConfigParser()
    config_files.read(enumerate_config_files())
    
    return Configuration(config_files)
