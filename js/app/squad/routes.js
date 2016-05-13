'use strict';

module.exports = {
  path: 'squad',

  getComponents(nextState, callback) {
    require.ensure([], (require) => {
      callback(null, require('./components/SquadEditor'));
    });
  },
};
