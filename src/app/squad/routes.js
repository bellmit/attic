import { bindActionCreators } from 'redux'

import * as actions from './actions'

function routes(store, api) {
  var dispatch = bindActionCreators(actions, store.dispatch)

  return {
    path: 'squad',
    onEnter() {
      dispatch.loadSquad(api)
    },
    component: require('./components/SquadEditor'),
  }
}

module.exports = routes
