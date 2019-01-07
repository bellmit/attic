import os
import os.path
import errno
import itertools
import contextlib
import subprocess

DEFAULT_EDITOR = "emacs" # suck it, vi nerds

def launch(path, initial_content=None):
    editor = resolve_editor()
    if initial_content is not None:
        with open(path, 'wt') as file:
            file.write(initial_content)
    
    subprocess.call([editor, path])

def resolve_editor():
    editor = os.getenv("EDITOR")
    if editor is None:
        return DEFAULT_EDITOR
    return editor
