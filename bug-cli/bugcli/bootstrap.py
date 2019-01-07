from bugcli import ui
from bugcli import config
from bugcli import auth as a
from bugcli import trackers

def terminal(argparse_callback):
    tui = ui.Terminal()
    cfg = config.load()
    tracker_factory = trackers.open_tracker(cfg)
    
    parser = argparse_callback()
    a.command_line_args(parser)
    tracker_factory.command_line_args(parser)
    
    args = parser.parse_args()
    cfg.apply_arguments(args)
    
    auth = a.Auth.from_config(cfg, tracker_factory.auth_section(cfg), tui)
    tracker = tracker_factory.from_config(cfg)
    
    return tui, auth, args, tracker
