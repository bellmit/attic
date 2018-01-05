const _ = require('lodash')
const lru = require("lru-cache")
const offerPump = require('./offerPump')

// A command reducer takes a state and a command, and produces a list of events.
// An event reducer takes a state and an event, and produces a new state. A game
// is a sequence of (events, state) pairs, one per frame, computed solely from
// the commands. (In principle, the game can be completely reconstructed from
// the list of commands going back to the beginning of time.)
//
// A Store mediates all ot this.
module.exports = function Store({storage, commandReducer, eventReducer, commandFrameAdvance, minimumRetainedFrames}) {
    async function stateAt(t, frame) {
        const [lastStoredFrame, lastStoredState] = await storage.lastStateAt(t, frame)
        const catchupEvents = await storage.frameRangeEvents(t, lastStoredFrame, frame)
        return catchupEvents.reduce(eventReducer, lastStoredState)
    }

    const saveState = offerPump(async (frame, state) => await storage.transaction(async t => {
        return await storage.addState(t, frame, state)
    }))

    const pruneHistory = offerPump(async frame => await storage.transaction(async t => {
        // We can't prune anything we might need to reconstruct states within
        // the retainedFrames window, so find the most recently stored state
        // that's _prior to_ the target frame, if there is one, and prune
        // everything in and before that state's frame.
        const [retainedFrame] = await storage.lastStateAt(t, frame)
        if (!retainedFrame)
            // No stored states at least minimumRetainedFrames old, don't prune
            // anything.
            return

        await storage.deleteBefore(t, retainedFrame)
    }))

    const eventsCache = lru(minimumRetainedFrames)
    const eventsPromises = {}

    function commandEventsReducer([state, events], command) {
        const nextEvents = commandReducer(state, command)
        const nextState = nextEvents.reduce(eventReducer, state)
        return [nextState, [...events, ...nextEvents]]
    }

    return {
        async findResumePoint(targetFrame) {
            return await storage.transaction(async t => {
                const currentFrame = await storage.currentFrame(t)
                // If there's no target, skip the client to the current frame
                // and resume starting with the next frame's events.
                if (!targetFrame)
                    return [currentFrame, currentFrame, await stateAt(t, currentFrame)]

                // If the target is earlier than the oldest frame we can
                // actually reconstruct, resume from the oldest state we have,
                // instead, and skip the client forwards in time.
                const [oldestFrame, oldestState] = await storage.oldestState(t)
                if (oldestFrame.gt(targetFrame))
                    return [oldestFrame, currentFrame, oldestState]

                // Asked for a state it's safe to resume from (no possibility of
                // lost events), so just resume from the next frame and
                // fast-forward through events up to the current frame.
                return [targetFrame, currentFrame, null]
            })
        },
        async addCommand(command) {
            return await storage.transaction(async t => {
                const currentFrame = await storage.currentFrame(t)
                const commandFrame = currentFrame.add(commandFrameAdvance)
                await storage.addCommand(t, commandFrame, command)
            })
        },
        async advanceFrame() {
            return await storage.transaction(async t => {
                const [prevFrame, nextFrame] = await storage.advanceFrame(t)
                if (!prevFrame || !nextFrame)
                    return null

                const prevState = await stateAt(t, prevFrame)
                const frameCommands = await storage.frameCommands(t, nextFrame)
                const [nextState, nextEvents] = frameCommands.reduce(commandEventsReducer, [prevState, []])

                const storeEvents = storage.addEvents(t, nextFrame, nextEvents)
                saveState(nextFrame, nextState)

                await storeEvents

                const prunedFrame = nextFrame.subtract(minimumRetainedFrames)
                pruneHistory(prunedFrame)

                return nextFrame
            })
        },
        async events(frame) {
            // Because this gets called every frame by every client, there's a
            // thundering herd of selects every frame - one per client - if this
            // query method is implemented the naive way. This approach reuses a
            // select if it's already in flight, on the assumption that the
            // table is write-once or and that it's therefore safe to share
            // reads between unrelated clients. Any previously-computed results
            // within the cache horizon are also reused without re-querying
            // them.
            const cacheKey = frame.toString()
            const cachedEvents = eventsCache.get(cacheKey)
            if (cachedEvents !== undefined)
                return cachedEvents
            if (cacheKey in eventsPromises)
                return await eventsPromises[cacheKey]
            const eventsPromise = eventsPromises[cacheKey] = storage.transaction(async t => {
                try {
                    const events = await storage.frameEvents(t, frame)

                    eventsCache.set(cacheKey, events)
                    return events
                } finally {
                    delete eventsPromises[cacheKey]
                }
            })
            return await eventsPromise
        },
    }
}
