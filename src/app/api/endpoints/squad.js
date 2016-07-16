'use strict'

function squad(api) {
  return {
    get() {
      return api.get('/squad')
    },

    update(squad) {
      return api.post('/squad', squad)
    },
  }
}

module.exports = {
  squad() {
    return squad(this)
  },
}
