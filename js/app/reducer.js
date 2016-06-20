'use strict';

import { handleActions } from 'redux-actions';

module.exports = handleActions({
  LANDING_LOADED: (state, action) => ({
    ...state,
    loading: false,
  }),
}, {
  loading: true,
});
