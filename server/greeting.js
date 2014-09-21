'use strict';

var express = require('express');

var route = express.Router();

route.get('', function(req, res) {
	res.send('Hello, world!');
});

module.exports = route;