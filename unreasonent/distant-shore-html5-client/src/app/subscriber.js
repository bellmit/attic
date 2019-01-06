'use strict'

import subscribe from './subscribe'

module.exports = function makeSubscriber(api) {
  return subscribe({
    lock: require('./lock/subscriber')(api),
  })
}
