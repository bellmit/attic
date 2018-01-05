module.exports = function offerPump(acceptor) {
    let offered = null
    let promise = null

    async function consumeOffered() {
        try {
            while (offered) {
                const consuming = offered
                offered = null
                await acceptor(...consuming)
            }
        } finally {
            promise = null
        }
    }

    return function offer(...args) {
        offered = args
        if (promise == null)
            promise = consumeOffered()
    }
}
