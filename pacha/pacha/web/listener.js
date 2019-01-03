const express = require('express')
const sse = require('sse-express')
const makeRouter = require('express-promise-router')
const bigInt = require('big-integer')

module.exports = function(store, pubsub) {
    const router = makeRouter()
    router.use(express.json())

    router.post('/command', async (req, res) => {
        await store.addCommand(req.body)
        res.status(204).end()
    })

    async function sendEvents(res, frame) {
        const events = await store.events(frame)
        if (events.length > 0)
            res.sse({
                id: frame,
                event: 'events',
                data: events,
            })
    }

    router.get('/events', sse(), async (req, res) => {
        const targetFrame = res.sse.lastEventId ? bigInt(res.sse.lastEventId) : null
        const [resumeFrame, currentFrame, state] = await store.findResumePoint(targetFrame)

        // Resync, if necessary…
        if (state)
            res.sse({
                id: resumeFrame,
                event: 'state',
                data: state,
            })

        // … then catch up on outstanding events …
        let frame = resumeFrame.next()
        for (; frame.lesserOrEquals(currentFrame); frame = frame.next())
            await sendEvents(res, frame)

        // … then start listening for subsequent frames …
        const listener = async message => {
            const nextFrame = message.frame
            for (; frame.lesserOrEquals(nextFrame); frame = frame.next())
                await sendEvents(res, frame)
        }
        pubsub.addChannel('frame', listener)
        res.on('close', () => pubsub.removeChannel('frame', listener))
    })

    return router
}
