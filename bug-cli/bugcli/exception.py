class TrackerError(Exception):
    def __init__(self, message, details=None):
        self.message = message
        self.details = details
