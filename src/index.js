import React from 'react'
import ReactDOM from 'react-dom'
import { createStore, applyMiddleware, combineReducers } from 'redux'
import { Provider } from 'react-redux'
import ReduxThunk from 'redux-thunk'

import socket from 'socket.io-client'

import { reducers } from './model'
import { App } from './components'
import { connect } from './relay'

const store = createStore(
  combineReducers(reducers),
  applyMiddleware(
    ReduxThunk
  )
)

const relay = socket()

connect(relay, store)

ReactDOM.render(
  <Provider store={store}>
    <App relay={relay} />
  </Provider>,
  document.getElementById('app')
)
