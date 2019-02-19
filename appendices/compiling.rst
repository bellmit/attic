Compiling this Document
=======================

Under the hood, this document is a collection of reStructuredText files, built
by the `Sphinx`_ documentation tool to produce the final document. For the most
part, this is automated.

.. _Sphinx: http://www.sphinx-doc.org/en/master/

Prerequisites
-------------

Certain software is required before you can build this document from its
sources. The following table breaks down the dependencies, and provides
installation instructions for the `Homebrew`_ package management system for
macOS. (Installation of Homebrew is beyond the scope of this document.)

.. _Homebrew: https://brew.sh/

===========  ===================  ===========================
Dependency   Version              Homebrew Command
===========  ===================  ===========================
`Python`_    3.7 or later         ``brew install python3``
`Pipenv`_    2018.11.26 or later  ``brew install pipenv``
`GNU Make`_  (Any)                N/A - Preinstalled on macOS
===========  ===================  ===========================

.. _Python: https://www.python.org
.. _Pipenv: https://pipenv.readthedocs.io/en/latest/
.. _GNU Make: https://www.gnu.org/software/make/

.. warning::

   The Python dependencies used by this document are managed by Pipenv.
   However, some services, including `Read the Docs`_, do not yet support
   Pipenv, and rely on a ``requirements.txt`` file for dependency management,
   instead.

   .. _Read the Docs: https://readthedocs.org/

   The manuscript for this document includes a ``requirements.txt`` file, but
   it must be updated any time a dependency is added, updated, or removed from
   this project.

   If you run any variation of

   * ``pipenv install``;

   * ``pipenv remove``;

   * ``pipenv lock``; or

   * ``pipenv sync``

   Then you **must** run ``pipenv run make requirements.txt`` immediately
   afterwards to ensure the changes are reflected in ``requirements.txt``.

   There is presently no way to automate this in Pipenv, or I would have
   automated it. However, there is an issue filed against Read the Docs to add
   support for Pipfile, which would mean that ``requirements.txt`` is no longer
   necessary, and that the steps to update it can be removed.

Compilation
-----------

All of Sphinx's `build commands`_ are supported, provided that you run them
with ``pipenv run``. This will automatically ensure that the required Python
dependencies, including Sphinx itself, are properly installed.

.. _build commands: http://www.sphinx-doc.org/en/master/usage/quickstart.html#running-the-build

Run ``pipenv run make dirhtml`` to compile the document into a collection of
HTML files. These files will be written to the ``_build/dirhtml`` directory.

Run ``pipenv run phix`` to start a service that automatically recompiles this
document whenever one of the manuscript files is changed. This will also start
a local web server, listening on ``http://localhost:8000/``, allowing you to
view the document in a browser easily.
