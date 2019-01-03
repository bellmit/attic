const { createActions } = require('redux-actions')

module.exports = createActions({
    PLAYER_JOINED: name => ({ name }),
})
