'use strict'

module.exports = {
  path: '/',
  component: require('./components/App'),
  indexRoute: {
    component: require('./components/Landing'),
  },
  childRoutes: [
    require('./squad/routes'),
  ],
}
