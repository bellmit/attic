'use strict'

var path = require('path')
var http = require('http')
var express = require('express')
var compression = require('compression')
var morgan = require('morgan')
var clientConfig = require('./client-config')

var app = express()

app.use(compression())

// If PORT is set, assume we're on Heroku and let Heroku log requests.
if (!process.env.PORT) {
  // Make it possible to diagnose request-level problems locally
  app.use(morgan('dev'))
}

// Serve assets right out of the dist tree.
app.use('/bundle', express.static(path.resolve('dist/bundle'), { maxAge: '1 year' }))

// HTML5 pushstate routing support. By the time we get here, we definitely did
// not match an asset. Serve the index.html document, instead.
app.get(/.*/, clientConfig(path.resolve('dist/index.html')))

var port = process.env.PORT || 4000
app.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`)
})
