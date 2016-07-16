'use strict'

var fs = require('fs')
var path = require('path')
var http = require('http')
var express = require('express')
var compression = require('compression')
var morgan = require('morgan')
var handlebars = require('handlebars')
var socketIo = require('socket.io')
var logging = require('./logging')

var port = process.env.PORT || 4000

var clientConfig = require('./clientConfig')
var indexContext = {
  clientConfig: JSON.stringify(clientConfig),
}

var app = express()

app.use(compression())

// If PORT is set, assume we're on Heroku and let Heroku log requests.
if (!process.env.PORT) {
  // Make it possible to diagnose request-level problems locally
  app.use(morgan('dev'))
}

// Serve assets right out of the dist tree.
app.use('/bundle', express.static('dist/bundle', { maxAge: '1 year' }))
app.use('/assets', express.static('dist/assets'))

// HTML5 pushstate routing support. By the time we get here, we definitely did
// not match an asset. Serve the index.html document, instead.
app.get(/.*/, function(req, res) {
  // We could precompile this, but live reload is nice in development.
  fs.readFile('dist/index.html', 'UTF-8', function(err, template) {
    if (err) throw err
    var renderer = handlebars.compile(template)
    var body = renderer(indexContext)
    res.send(body)
  })
})

var server = http.Server(app)
var io = socketIo(server)
logging(io)

server.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`)
})
