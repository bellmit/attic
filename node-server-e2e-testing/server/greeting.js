'use strict';

var express = require('express');

var route = express.Router();

route.get('', function(req, res) {
	var name = req.query.name || 'world';
	var greeting = [
		'Hello, ',
		name,
		'!'
	].join('');
	res.send(greeting);
});

module.exports = route;