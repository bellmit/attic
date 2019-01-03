import { createAction, handleActions } from 'redux-actions'

const stateLoaded = createAction('STATE_LOADED')
const switchTab = createAction('SWITCH_TAB')

function loadState(api) {
    return dispatch =>
        api.state()
            .then(json => dispatch(stateLoaded(json)))
}

export default {
    // Begin loading the current state from the service.
    loadState,
    // Switch the displayed tab.
    switchTab,
}

export const reducer = handleActions({
    [stateLoaded]: (state, action) => ({
        ...state,
        ...action.payload,
    }),
    [switchTab]: (state, action) => ({
        ...state,
        tab: action.payload,
    }),
}, {
    // The identity of the selected tab.
    tab: 'state',
    // The loaded state.
    state: {},
    // The loaded event list.
    events: [],
    // The loaded warning list.
    warnings: [],
})
