from setuptools import setup, find_packages

setup(
    name='redpaste',
    version='0.dev',
    
    packages=find_packages(),
    include_package_data=True,
    
    install_requires=[
        'Werkzeug',
        'redfox',
        'sqlalchemy',
        'mako',
        'Pygments',
        'setuptools'
    ],

    entry_points = {
        'paste.app_factory': [
            'pastebin=redpaste.app:Pastebin'
        ]
    }
)
