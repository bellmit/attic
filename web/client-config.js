'use strict'

/* -- defaults -- */
var config = {
  API_URL: 'http://localhost:5000',
  AUTH0_DOMAIN: 'unreasonent.auth0.com',
  AUTH0_CLIENT_ID: 'F3EFeKAOmYjNsqgYJ3yEo23ejqHXdlJK',
}

/* -- load exported config values from env, if set -- */
var keys = require('lodash/keys')

keys(config).forEach(function(key) {
  var env = process.env[key]
  if (env)
    config[key] = env
})

/* -- provide the requisite middleware (factory) -- */
var fs = require('fs')
var handlebars = require('handlebars')

var templateContext = {
  clientConfig: JSON.stringify(config),
}

function makeMiddleware(templatePath) {
  return function sendConfiguredTemplate(req, res) {
    // We could precompile this, but live reload is nice in development.
    fs.readFile(templatePath, 'UTF-8', function(err, template) {
      if (err) throw err
      var renderer = handlebars.compile(template)
      var body = renderer(templateContext)
      res.send(body)
    })
  }
}

module.exports = makeMiddleware
