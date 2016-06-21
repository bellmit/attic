'use strict'

var http = require('http')
var path = require('path')
var express = require('express')
var compression = require('compression')
var morgan = require('morgan')

var port = process.env.PORT || 4000

var app = express()

// Make it possible to diagnose request-level problems locally
app.use(morgan('dev'))
// Enable asset compression on the fly
app.use(compression())

// Serve assets right out of the dist tree, and cache them aggressively.
app.use('/bundle', express.static('dist/bundle', { maxAge: '1 year' }))

// HTML5 pushstate routing support. By the time we get here, we definitely did
// not match an asset. Serve the index.html document, instead.
app.get(/.*/, function(req, res) {
  res.sendFile(path.resolve('dist/index.html'))
})

var server = http.Server(app)
server.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`)
})
