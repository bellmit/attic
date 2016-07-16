'use strict'

// For some insane reason, React isn't auto-required by babel when compiling
// jsx element expressions.
import React from 'react'
import ReactDOM from 'react-dom'
import { createStore, applyMiddleware, combineReducers } from 'redux'
import { Provider } from 'react-redux'
import ReduxThunk from 'redux-thunk'
import { Router, browserHistory } from 'react-router'
import { syncHistoryWithStore, routerReducer, routerMiddleware } from 'react-router-redux'

import Auth0Lock from 'auth0-lock'

import Api, { ApiProvider } from 'app/api'
import { LockProvider} from 'app/lock/components'

import routes from './routes'
import reducers from './reducers'
import { mount as mountSubscriber } from './subscribe'
import subscriber from './subscriber'

const api = new Api()
const lock = new Auth0Lock(
  appConfig.AUTH0_CLIENT_ID,
  appConfig.AUTH0_DOMAIN
)

const store = createStore(
  combineReducers({
    ...reducers,
    routing: routerReducer,
  }),
  applyMiddleware(
    routerMiddleware(browserHistory),
    ReduxThunk
  )
)

const history = syncHistoryWithStore(browserHistory, store)

mountSubscriber(store, subscriber(api))

ReactDOM.render(

  <Provider store={store}>
    <ApiProvider api={api}>
      <LockProvider lock={lock}>
        <Router routes={routes} history={history} />
      </LockProvider>
    </ApiProvider>
  </Provider>,
  document.getElementById('app')
)
