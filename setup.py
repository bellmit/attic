from setuptools import setup, find_packages

setup(
    name='redfox',
    version='0.dev',
    
    packages=find_packages(),
    
    install_requires=[
        'Werkzeug',
    ],
    
    entry_points = {
        'paste.app_factory': [
            'example=redfox.examples:Example',
            'inherited=redfox.examples:InheritanceExample',
        ]
    },
)
