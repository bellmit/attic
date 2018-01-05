const express = require('express')
const sse = require('sse-express')
const makeRouter = require('express-promise-router')

module.exports = function(store, pubsub) {
    const router = makeRouter()
    router.use(express.json())

    router.post('/command', async (req, res) => {
        await store.addCommand(req.body)
        res.status(204).end()
    })

    router.get('/events', sse(), async (req, res) => {
        const send = async frame => {
            const events = await store.events(frame)
            if (events.length > 0)
                res.sse({
                    id: frame,
                    event: 'events',
                    data: events,
                })
        }

        const currentFrame = await store.currentFrame()
        let frame = Math.min(res.sse.lastEventId || currentFrame, currentFrame)

        const state = await store.state(frame)
        res.sse({
            id: frame,
            event: 'state',
            data: state,
        })
        for (; frame <= currentFrame; ++frame)
            await send(frame)

        const listener = async message => {
            const nextFrame = message.frame
            for (; frame <= nextFrame; ++frame)
                await send(frame)
        }
        pubsub.addChannel('frame', listener)
        res.on('close', () => pubsub.removeChannel('frame', listener))
    })

    return router
}
