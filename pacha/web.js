const express = require('express')
const listener = require('./listener')
const factories = require('./factories')

const app = express()

const store = factories.store()
const pubsub = factories.pubsub()

const router = listener(store, pubsub)

app.use(router)

const port = process.env.PORT || 3000
app.listen(port, () => {
    console.log('web', `port=${port}`)
})
