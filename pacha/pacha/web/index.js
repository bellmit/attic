const express = require('express')
const factories = require('../factories')
const listener = require('./listener')
const client = require('./client')

const app = express()

const store = factories.store()
const pubsub = factories.pubsub()

const router = listener(store, pubsub)

app.use('/api', router)
app.use('/', client)

const port = process.env.PORT || 3000
app.listen(port, () => {
    console.log('web', `port=${port}`)
})
