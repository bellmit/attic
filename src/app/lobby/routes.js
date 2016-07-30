'use strict'

module.exports = {
  childRoutes: [
    {
      path: 'lobby/challenge/:id',
      component: require('./components/Challenge'),
    },
    {
      path: 'lobby/ranked',
      component: require('./components/Ranked'),
    },
  ],
}
