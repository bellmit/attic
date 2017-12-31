const PGPubsub = require('pg-pubsub')
const model = require('./model')
const Store = require('./store')
const reducers = require('./reducers')

module.exports = {
    store() {
        return Store(model, reducers.state, reducers.event)
    },
    pubsub() {
        return new PGPubsub(process.env.DATABASE_URL, {
            log: () => {},
        })
    }
}
