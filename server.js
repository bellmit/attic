'use strict';

var express = require('express');
var app = express();

// Mount routes
app.use('/greet', require('./server/greeting'));

var server = app.listen(5000, function() {
	if (process.send) {
		/* 
		 * We are being run through child_process.fork; notify the parent that
		 * we're ready to accept requests.
		 */
		process.send('started');
	}
});
