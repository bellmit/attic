from setuptools import setup, find_packages

import wallclock

setup(
    name='wallclock',
    version='1.1.dev',
    author='Owen Jacobson',
    author_email='owen.jacobson@grimoire.ca',
    url='http://bitbucket.org/ojacobson/wallclock',
    
    description='A simple stack-based performance logger',
    long_description=wallclock.__doc__,
    
    license='MIT License',
    
    classifiers=[
        'Intended Audience :: Developers',
        'Intended Audience :: System Administrators',
        'License :: OSI Approved :: MIT License',
        'Operating System :: OS Independent',
        'Topic :: Software Development :: Debuggers',
        'Topic :: System :: Logging',
    ],
    
    py_modules=['wallclock'],
    
    setup_requires=[
        'setuptools_hg'
    ],
    install_requires=[
        'decorator'
    ],
)
