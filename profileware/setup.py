try:
    from setuptools import setup, find_packages
except ImportError:
    import ez_setup
    ez_setup.use_setuptools()
    from setuptools import setup, find_packages

setup(
    name='profileware',
    version='1.0',
    author='Owen Jacobson',
    author_email='owen.jacobson@grimoire.ca',
    description='Profiler middleware for Paster',

    py_modules=['profileware'],
    package_dir={
        '': 'src',
    },

    setup_requires=[
        'Sphinx',
        'pkginfo'
    ],
    install_requires=[
        'functional'
    ],

    entry_points = {
        'console_scripts': [
            'profcat = profileware:show_stats',
        ],
        'paste.filter_factory': [
            'profile = profileware:filter',
        ]
    }
)
