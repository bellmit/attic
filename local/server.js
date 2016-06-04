'use strict';

var fs = require('fs');
var path = require('path');
var express = require('express');
var morgan = require('morgan');
var handlebars = require('handlebars');

var port = process.env.PORT || 4000;

var clientConfig = require('./clientConfig');
var indexContext = {
  clientConfig: JSON.stringify(clientConfig),
};

var app = express();

// If PORT is set, assume we're on Heroku and let Heroku log requests.
if (!process.env.PORT) {
  // Make it possible to diagnose request-level problems locally
  app.use(morgan('dev'));
}

// Serve assets right out of the dist tree.
app.use('/js', express.static('dist/js', { maxAge: '1 year' }));
app.use('/css', express.static('dist/css'));
app.use('/fonts', express.static('dist/fonts'));
app.use('/assets', express.static('dist/assets'));

// HTML5 pushstate routing support. By the time we get here, we definitely did
// not match an asset. Serve the index.html document, instead.
app.get(/.*/, function(req, res) {
  // We could precompile this, but live reload is nice in development.
  fs.readFile('dist/index.html', 'UTF-8', function(err, template) {
    if (err) throw err;
    var renderer = handlebars.compile(template);
    var body = renderer(indexContext);
    res.send(body);
  });
});

app.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`);
});
