module.exports = function(store, pubsub) {
    return async function tick() {
        const frame = await store.advanceFrame()
        if (frame != null)
            await pubsub.publish('frame', {
                frame,
            })
    }
}
