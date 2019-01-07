from setuptools import setup, find_packages

setup(
    name='bug-cli',
    version='1.0.dev',
    author='Owen Jacobson',
    author_email='owen.jacobson@grimoire.ca',
    
    description='CLI interface for simple bug tracking workflows',
    
    packages=find_packages(),
    scripts=[
        'open-bug',
        'comment-bug',
        'resolve-bug'
    ],
    
    install_requires=[
        'requests'
    ],
    
    entry_points={
        'bugcli.tracker': [
            'jira = bugcli.tracker.jira:Jira',
            'redmine = bugcli.tracker.redmine:Redmine',
            'bitbucket = bugcli.tracker.bitbucket:Bitbucket',
            'github = bugcli.tracker.github:GitHub'
        ]
    }
)
