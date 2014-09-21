'use strict';

var request = require('request');

describe("The /greet endpoint", function () {
	runServer();

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
