# You can set these variables from the command line.
SPHINXOPTS    =
SPHINXBUILD   = sphinx-build
SOURCEDIR     = .
BUILDDIR      = _build

# Put it first so that "make" without argument is like "make help".
help:
	@$(SPHINXBUILD) -M help "$(SOURCEDIR)" "$(BUILDDIR)" $(SPHINXOPTS) $(O)

.PHONY: help Makefile Pipfile Pipfile.lock

# requirements.txt in this project is generated for ReadTheDocs' sake, as that
# service does not support Pipenv (yet).
#
# See: <https://github.com/rtfd/readthedocs.org/issues/3181>
requirements.txt: Pipfile Pipfile.lock Makefile
	pipenv lock --requirements > $@

# Catch-all target: route all unknown targets to Sphinx using the new
# "make mode" option.  $(O) is meant as a shortcut for $(SPHINXOPTS).
%: Makefile
	@$(SPHINXBUILD) -M $@ "$(SOURCEDIR)" "$(BUILDDIR)" $(SPHINXOPTS) $(O)
