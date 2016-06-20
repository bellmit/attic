'use strict';

import { createAction } from 'redux-actions';
import { push } from 'react-router-redux';

export function openSquadIfNeeded(api) {
  return dispatch =>
    api.squad().get()
      .catch(err => {
        if (err.code == 404)
          dispatch(push('/squad'));
        return Promise.reject(err);
      })
      .then(() => dispatch(landingLoaded()));
}

const landingLoaded = createAction('LANDING_LOADED');
