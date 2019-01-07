#!/usr/bin/env python
from ez_setup import use_setuptools
use_setuptools()

from setuptools import setup

setup(name='hacksign',
      version='1.0-SNAPSHOT',
      author='Owen Jacobosn',
      author_email='owen.jacobson@grimoire.ca',
      url='http://alchemy.grimoire.ca/hg/hacksign',
      description='A python driver for bmix and the hacklab LED sign.',
      packages=['hacksign'],
      scripts=['snow','steev','blank','steevilepsy'],
      test_suite='test.testsuite'
      )
