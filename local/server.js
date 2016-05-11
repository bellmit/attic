'use strict';

var path = require('path');
var express = require('express');
var morgan = require('morgan');

var port = process.env.PORT || 4000;

var app = express();

// Make it possible to diagnose request-level problems.
app.use(morgan('dev'));

// Serve assets right out of the dist tree.
app.use(express.static('dist'));

// HTML5 pushstate routing support. By the time we get here, we definitely did
// not match an asset. Serve the index.html document, instead.
app.get(/.*/, function(req, res) {
  res.sendFile(path.resolve('dist/index.html'));
});

app.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`);
});
