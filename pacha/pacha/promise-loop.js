const heartbeats = require('heartbeats')

// Run "fn", a function which produces a promise, approximately every
// `intervalMillis` milliseconds. Avoid running `fn` while it's already running;
// if a call to `fn` takes longer than `intervalMillis` to complete, this will
// attempt to "catch up" by running `fn` immediately.
module.exports = function loop(fn, intervalMillis) {
    const heart = heartbeats.createHeart(intervalMillis)
    const pulse = heart.createPulse()

    const tick = async () => {
        pulse.beat()
        await run(0)
    }

    const run = async beat => {
        try {
            await fn()
        } catch(e) {
            console.error('failed-tick', e)
        }
        if (beat < pulse.missedBeats) {
            console.warn('falling-behind', `beat=${beat}`, `goal=${pulse.missedBeats}`)
            return run(beat + 1)
        }
        schedule()
    }

    const schedule = () => {
        heart.createEvent(1, {countTo: 1}, tick)
    }

    return schedule()
}
