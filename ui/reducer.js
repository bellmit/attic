import { combineReducers } from 'redux'
import { routerReducer } from 'react-router-redux'

import * as documents from './documents'
import * as doc from './document'
import * as submit from './submit'
import * as state from './state'

// Constructs the root reducer for Cadastre. This is a combination of a number
// of view-specific reducers; Cadastre's UI retains state for all UI elements at
// all times, even when displaying a limited view.
export default function createReducer() {
    return combineReducers({
        documents: documents.reducer,
        document: doc.reducer,
        submit: submit.reducer,
        state: state.reducer,
        router: routerReducer,
    })
}
