'use strict'

import { handleActions } from 'redux-actions'

module.exports = handleActions({
  LANDING_LOADED: (state, action) => ({
    ...state,
    ...action.payload,
    loading: false,
  }),
}, {
  loading: true,
  squadNeeded: null,
})
