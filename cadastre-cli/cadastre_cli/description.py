import getpass
import socket

def description():
    return 'cadastre-cli ({username} on {hostname})'.format(
        username=getpass.getuser(),
        hostname=socket.gethostname(),
    )
