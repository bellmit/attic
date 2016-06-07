'use strict';

import { handleActions } from 'redux-actions';

var initialLock = {
  idToken: null,
  profile: null,
  userId: null,
};

module.exports = handleActions({
  LOCK_SUCCESS: (state, action) => ({
    ...state,
    ...action.payload,
  }),
  LOCK_CLEAR: (state, action) => initialLock,
}, initialLock);
