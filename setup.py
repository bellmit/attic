from setuptools import setup, find_packages

setup(
    name="cadastre-cli",
    version="0.1",
    packages=find_packages(),
    scripts=['bin/cadastre'],

    install_requires=[
        'dateparser',
        'requests',
        'PyYAML',
    ]
)
