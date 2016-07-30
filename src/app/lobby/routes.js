'use strict'

module.exports = {
  childRoutes: [
    {
      path: 'challenge/:id',
      component: require('./components/Challenge'),
    },
    {
      path: 'ranked',
      component: require('./components/Ranked'),
    },
  ],
}
