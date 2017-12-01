import { createStore, applyMiddleware } from 'redux'
import thunkMiddleware from 'redux-thunk'
import { routerMiddleware } from 'react-router-redux'
import createReducer from './reducer'

// Constructs the Redux root store for this application. This ensures that Redux
// features used in this app, such as thunk actions, are available, and wires up
// an instance of the top-level reducer.
export default function store(history) {
    return createStore(
        createReducer(),
        applyMiddleware(
            thunkMiddleware,
            routerMiddleware(history),
        ),
    )
}
