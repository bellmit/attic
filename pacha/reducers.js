module.exports = {
    state(state, command) {
        return [command]
    },

    event(state, event) {
        return {
            ...state,
            events: [...(state.events || []), event],
        }
    },
}
