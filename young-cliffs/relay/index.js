const path = require('path')
const express = require('express')
const compression = require('compression')
const morgan = require('morgan')

const app = express()

const webrootDir = path.resolve('webroot')
const bundleDir = path.resolve(webrootDir, 'bundle')

app.use(compression())

// If PORT is set, assume we're on Heroku and let Heroku log requests.
if (!process.env.PORT) {
  // Make it possible to diagnose request-level problems locally
  app.use(morgan('dev'))
}

// Serve assets right out of the webroot tree.
app.use('/bundle', express.static(bundleDir, { maxAge: '1 year' }))
app.use('/', express.static(webrootDir))

var port = process.env.PORT || 4000
app.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`)
})
