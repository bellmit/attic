'use strict';

var child = require('child_process');

function runServer() {
	var server;

	beforeEach(function(done) {
		server = child.fork('server.js');
		server.on('message', function(message) {
			if (message == 'started') {
				done();
			}
		});
	});

	afterEach(function(done) {
		server.on('exit', done);
		server.kill('SIGTERM');
	});
}

module.exports = {
	runServer: runServer
};
