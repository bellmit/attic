from setuptools import setup, find_packages

setup(
    name='crankshaft',
    version='0.dev',
    
    packages=find_packages(),
    
    install_requires=[
        'Werkzeug',
    ],
    
    entry_points = {
        'paste.app_factory': [
            'example=crankshaft.examples:Example',
            'inherited=crankshaft.examples:InheritanceExample',
        ]
    },
)
