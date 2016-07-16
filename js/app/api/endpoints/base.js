'use strict'

function mergeHeaders(headers, token) {
  var merged = new Headers({
    'Accept': 'application/json',
  })

  if (headers)
    for (var [name, value] of headers.entries())
      merged.append(name, value)

  if (token)
    merged.append('Authorization', `Bearer ${token}`)

  return merged
}

const decoders = {
  'application/json': response => response.json(),
}

const defaultDecoder = Promise.resolve.bind(Promise)

function decode(response) {
  var contentType = response.headers.get('Content-Type')
  var decoder = decoders[contentType] || defaultDecoder

  return decoder(response)
}

function apiResponse(response) {
  if (!response.ok)
    return decode(response)
      .then(Promise.reject.bind(Promise))
  return decode(response)
}

module.exports = {
  fetch(path, options = {}) {
    var url = this.resolve(path)

    var options = {
      ...options,
      headers: mergeHeaders(options.headers, this.token),
    }

    return window.fetch(url, options)
      .then(apiResponse)
  },

  get(path, options) {
    return this.fetch(path, options)
  },

  post(path, entity, options = {}) {
    var request = {
      method: 'POST',
      headers: new Headers({
        'Content-Type': 'application/json',
      }),
      body: JSON.stringify(entity),
    }
    return this.fetch(path, request)
  }
}
