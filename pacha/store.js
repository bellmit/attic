// A command reducer takes a state and a command, and produces a list of events.
// An event reducer takes a state and an event, and produces a new state. A game
// is a sequence of (events, state) pairs, one per frame, computed solely from
// the commands. (In principle, the game can be completely reconstructed from
// the list of commands going back to the beginning of time.)
//
// A Store mediates all ot this.
module.exports = function Store(storage, commandReducer, eventReducer) {
    return {
        async currentFrame() {
            return await storage.transaction(async t => {
                const frameInfo = await storage.frame(t)
                return frameInfo.current_frame
            })
        },
        async addCommand(command) {
            return await storage.transaction(async t => {
                let frameInfo = await storage.frame(t)
                await storage.addCommand(t, frameInfo.command_frame, command)
            })
        },
        async advanceFrame() {
            return await storage.transaction(async t => {
                const [nextFrame, prevFrame] = await storage.advanceFrame(t)
                if (!nextFrame && !prevFrame)
                    return null
                const commands = await storage.frameCommands(t, nextFrame)
                const state = await storage.frameState(t, prevFrame)
                const events = flatMap(commands, command => commandReducer(state, command))
                const storeEvents = storage.addEvents(t, nextFrame, events)
                const nextState = events.reduce(eventReducer, state)
                const storeState = storage.addState(t, nextFrame, nextState)
                await Promise.all([storeEvents, storeState])
                return nextFrame
            })
        },
        async state(frame) {
            return await storage.transaction(async t => {
                return await storage.frameState(t, frame)
            })
        },
        async events(frame) {
            return await storage.transaction(async t => {
                return await storage.frameEvents(t, frame)
            })
        },
    }
}

function flatMap(array, f) {
    return Array.prototype.concat.apply([], array.map(f))
}
