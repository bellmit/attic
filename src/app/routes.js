import { bindActionCreators } from 'redux'

import * as actions from 'app/actions'
import * as lockActions from 'app/lock/actions'

function routes(store, api, lock) {
  var dispatch = bindActionCreators({
    ...actions,
    ...lockActions,
  }, store.dispatch)

  return {
    path: '/',
    onEnter() {
      dispatch.boot(api, lock)
    },
    component: require('./components/App'),
    indexRoute: {
      onEnter() {
        dispatch.detectSquadNeeded(api)
      },
      component: require('./components/Landing'),
    },
    childRoutes: [
      require('./battle/routes'),
      require('./lobby/routes'),
      require('./squad/routes')(store, api),
    ],
  }
}

module.exports = routes
