const { combineReducers } = require('redux')
const { handleActions } = require ('redux-actions')
const commands = require('./commands')
const events = require('./events')

module.exports = {
    // This is something of a cheat. So long as all possible commands are
    // covered, this always produces an event list. If any command not included
    // in this reducer map is ever submitted, handleActions will throw up its
    // hands and do something perfectly reasonable for a redux context: it'll
    // return the _state_.
    //
    // Obviously wrong, needs a better fix because commands come from players
    // over the network and can be garbage.
    state: handleActions({
        [commands.joinGame]: (state, { payload }) => state.players && payload.name in state.players ?
                // This player is already present.
                [] :
                // Hello, beautiful. Let me announce you in.
                [events.playerJoined(payload.name)],
    }, []),

    event: combineReducers({
        players: handleActions({
            // Don't need to check if we already have this player: the command
            // reducer is presumptively telling the truth, and if it's lying, we
            // get sensible results anyways.
            [events.playerJoined]: (state, event) => ({
                ...state,
                [event.payload.name]: {},
            }),
        }, {}),
    }),
}
