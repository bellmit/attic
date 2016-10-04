const http = require('http')
const path = require('path')
const express = require('express')
const compression = require('compression')
const morgan = require('morgan')
const socketIo = require('socket.io')
const relay = require('./moo-relay')

const port = process.env.PORT || 4000
const mooHost = process.env.MOO_HOST
const mooPort = process.env.MOO_PORT
// UTF-8 would actually be the wrong default, here. LambdaMOO predates
// widespread deployment of UTF-8, and in any case, speaks ASCII exclusively.
// Do not even consider using a multibyte encoding until you figure out how to
// properly handle xterm-color escapes & signals.
const mooEncoding = process.env.MOO_ENCODING || 'Latin-1'

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

const server = http.Server(app)
const io = socketIo(server, {
  serveClient: false,
})

io.on('connection', relay(mooHost, mooPort, mooEncoding))

server.listen(port, function() {
  console.log("started", `port=${port}`, `url=http://localhost:${port}/`)
})
