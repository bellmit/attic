const { createActions } = require('redux-actions')

module.exports = createActions({
    JOIN_GAME: name => ({ name }),
})
