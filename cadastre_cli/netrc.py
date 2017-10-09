from .vendor import netrc
import os
import shutil

def update_netrc(host, username, password):
    path = os.path.expanduser('~/.netrc')

    try:
        table = netrc.netrc(path)
    except FileNotFoundError:
        # Create the file, with sane permissions
        os.close(os.open(path, os.RDWR | os.CREAT, 0o700))
        table = netrc.netrc(path)

    table.hosts[host] = (username, None, password)

    tmp_path = path + '.tmp'
    with open(tmp_path, 'x') as file:
        shutil.copystat(path, tmp_path)
        file.write(str(table))
    os.rename(tmp_path, path)
