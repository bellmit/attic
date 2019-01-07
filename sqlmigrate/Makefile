
test:
	./py/bin/nosetests

bootstrap:
	[ -f `which virtualenv` ] || { sudo apt-get install virtualenv; }
	virtualenv --no-site-packages py
	./py/bin/easy_install nose
	./py/bin/python setup.py develop

pypi_upload:
	./py/bin/python setup.py register
	./py/bin/python setup.py sdist bdist_egg upload

.PHONY: test bootstrap
