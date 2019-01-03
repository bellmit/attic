const PGPubsub = require('pg-pubsub')
const model = require('./model')
const Store = require('./store')
const reducers = require('./reducers')

module.exports = {
    store() {
        return Store({
            storage: model,
            commandReducer: reducers.state,
            eventReducer: reducers.event,
            commandFrameAdvance: process.env.COMMAND_FRAME_ADVANCE || 2,
            minimumRetainedFrames: process.env.MINIMUM_RETAINED_FRAMES || 200,
        })
    },
    pubsub() {
        return new PGPubsub(process.env.DATABASE_URL, {
            log: () => {},
        })
    }
}
