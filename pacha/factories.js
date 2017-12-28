const PGPubsub = require('pg-pubsub')
const model = require('./model')
const Store = require('./store')

module.exports = {
    store() {
        return Store(model, (state, command) => [command], (state, event) => ({
            ...state,
            events: [...(state.events || []), event],
        }))
    },
    pubsub() {
        return new PGPubsub(process.env.DATABASE_URL, {
            log: () => {},
        })
    }
}
