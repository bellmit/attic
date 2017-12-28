const factories = require('./factories')
const makeTick = require('./tick')
const loop = require('./promise-loop')

const store = factories.store()
const pubsub = factories.pubsub()

const tick = makeTick(store, pubsub)

const period = process.env.TICK_PERIOD || 50
loop(tick, period)

console.log('engine', `period=${period}`)
