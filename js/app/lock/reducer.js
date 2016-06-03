'use strict';

import { combineReducers } from 'redux';
import { handleActions } from 'redux-actions';

const idToken = handleActions({
  LOCK_SUCCESS: (state, action) => action.payload.idToken || state,
  LOCK_CLEAR: (state, action) => null,
}, null);

const profile = handleActions({
  LOCK_SUCCESS: (state, action) => action.payload.profile || state,
  LOCK_CLEAR: (state, action) => null,
}, null);

module.exports = combineReducers({
  idToken,
  profile,
});
