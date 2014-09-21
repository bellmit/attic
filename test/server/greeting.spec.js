'use strict';

var child = require('child_process');
var request = require('request');

describe("The /greet endpoint", function () {
	var server;

	beforeEach(function(done) {
		server = child.fork("server.js");
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

	it("provides a generic greeting by default", function(done) {
		request('http://localhost:5000/greet', function(error, response, body) {
			expect(body).toEqual('Hello, world!');
			done();
		});
	});

	it("provides a customized greeting when given a name", function(done) {
		request('http://localhost:5000/greet?name=Bob%20Dobbs', function(error, response, body) {
			expect(body).toEqual('Hello, Bob Dobbs!');
			done();
		});
	});
});
