def command_line_args(parser):
    group = parser.add_argument_group('authentication options')
    group.add_argument(
        '-U', '--username',
        metavar='USERNAME',
        help='Your bug tracker username.'
    )
    group.add_argument(
        '-P', '--password',
        metavar='PASSWORD',
        help='Your bug tracker username. This option has security risks; %(prog)s will prompt for the password if not set, which is more secure.'
    )

class Auth(object):
    @classmethod
    def from_config(cls, config, section, ui):
        auth = cls(ui)
        auth.username = config.get(section, 'username')
        auth.password = config.get(section, 'password')
        return auth
    
    def __init__(self, ui):
        self.ui = ui
        self.username = None
        self.password = None
    
    def http_basic(self):
        if self.username is not None:
            if self.password is None:
                self.password = self.ui.prompt_tracker_password()
            return (self.username, self.password)
        
        return None
