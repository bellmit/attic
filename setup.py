from setuptools import setup, find_packages
import os.path as p

with open(p.join(p.dirname(__file__), 'requirements.txt'), 'r') as r:
    requirements = r.readlines()

setup(
    name="alicorn",
    version="2",
    packages=find_packages(),
    install_requires=requirements,
    scripts=['scripts/alicorn'],
)
