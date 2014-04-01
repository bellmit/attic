from setuptools import setup, find_packages

setup(
    name     = "plugbox",
    version  = "0.0",
    packages = find_packages(),
    scripts  = [
        'bin/plugbox',
    ]
)
