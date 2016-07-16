'use strict'

import subscribe from 'app/subscribe'

module.exports = function makeSubscriber(api) {
  return subscribe({
    idToken: api.changeToken.bind(api),
  })
}
