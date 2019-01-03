function makeMiddleware(path) {
  return function defaultFile(req, res) {
    res.sendFile(path)
  }
}

module.exports = makeMiddleware
