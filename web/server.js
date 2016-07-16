'use strict'

var babel = require("babel-register")

var http = require('http')
var path = require('path')
var fs = require('fs')
var express = require('express')
var compression = require('compression')
var morgan = require('morgan')
var handlebars = require('handlebars')

var React = require('react')
var ReactDOM = require('react-dom/server')
var ReactRouter = require('react-router')
var routes = require('../src/routes')

var port = process.env.PORT || 4000

var app = express()

function route(req, res, cb) {
  ReactRouter.match(
    {routes: routes.routes, location: req.url},
    function(error, redirectLocation, renderProps) {
      if (error) {
        res.status(500).send(error.message)
      } else if (redirectLocation) {
        res.redirect(302, redirectLocation.pathname + redirectLocation.search)
      } else if (renderProps) {
        var router = React.createElement(ReactRouter.RouterContext, renderProps)
        var body = ReactDOM.renderToString(router)
        cb(body)
      } else {
        res.status(404).send('Not found')
      }
    }
  )

}

// Make it possible to diagnose request-level problems locally
app.use(morgan('dev'))
// Enable asset compression on the fly
app.use(compression())

// Serve assets right out of the dist tree, and cache them aggressively.
app.use('/bundle', express.static('dist/bundle', { maxAge: '1 year' }))

// HTML5 pushstate routing support. By the time we get here, we definitely did
// not match an asset. Serve based on react-router, instead.
app.get(/.*/, function(req, res) {
  route(req, res, function(body) {
    // We could precompile this, but live reload is nice in development.
    fs.readFile('dist/index.html', 'UTF-8', function(err, template) {
      if (err)
        throw err
      var renderer = handlebars.compile(template)
      var renderedBody = renderer({
        body: body,
      })
      res.send(renderedBody)
    })
  })
})

var server = http.Server(app)
server.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`)
})
