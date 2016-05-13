'use strict';

// For some insane reason, React isn't auto-required by babel when compiling
// jsx element expressions.
import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, combineReducers } from 'redux'
import { Provider } from 'react-redux'
import { Router, browserHistory } from 'react-router';

import routes from './routes';
import reducers from './reducers';

const store = createStore(
  combineReducers(reducers)
);

ReactDOM.render(
  <Provider store={store}>
    <Router routes={routes} history={browserHistory} />
  </Provider>,
  document.getElementById('app')
);
