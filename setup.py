from setuptools import setup, find_packages

setup(
    name='proto',
    version='0.dev',
    
    py_modules=['proto'],
    
    install_requires=[
        'Werkzeug',
    ],
    
    entry_points = {
        'paste.app_factory': [
            'example=proto:Example',
            'inherited=proto:InheritanceExample',
        ]
    },
)
