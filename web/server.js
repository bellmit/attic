'use strict'

var http = require('http')
var path = require('path')
var express = require('express')
var compression = require('compression')
var morgan = require('morgan')

var defaultFile = require('./default-file')

var app = express()

// Make it possible to diagnose request-level problems locally
app.use(morgan('dev'))

// Enable asset compression on the fly
app.use(compression())

// Serve assets right out of the dist tree, and cache them aggressively.
app.use('/bundle', express.static('dist/bundle', { maxAge: '1 year' }))

// HTML5 pushstate routing support. By the time we get here, we definitely did
// not match an asset. Serve the index.html document, instead.
app.get(/.*/, defaultFile(path.resolve('dist/index.html')))

var server = http.Server(app)

var port = process.env.PORT || 4000
server.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`)
})
