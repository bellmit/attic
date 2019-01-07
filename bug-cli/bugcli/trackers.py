import pkg_resources
from bugcli.tracker import jira

def open_tracker(config):
    tracker_type = config.require('tracker', 'type')
    return next(
        pkg_resources.iter_entry_points('bugcli.tracker', tracker_type)
    ).load()
