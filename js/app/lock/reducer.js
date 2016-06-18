'use strict';

import { handleActions } from 'redux-actions';

var initialLock = {
  booting: true,
  idToken: null,
  profile: null,
};

module.exports = handleActions({
  LOCK_SUCCESS: (state, action) => ({
    ...state,
    booting: false,
    ...action.payload,
  }),
  LOCK_CLEAR: (state, action) => ({
    ...initialLock,
    booting: false,
  }),
}, initialLock);
