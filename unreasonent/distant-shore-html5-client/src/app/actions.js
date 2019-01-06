'use strict'

import { createAction } from 'redux-actions'

export function detectSquadNeeded(api) {
  return dispatch =>
    api.squad().get()
      .then(() => dispatch(landingLoaded({
        squadNeeded: false,
      })))
      .catch(err => {
        if (err.code == 404)
          return dispatch(landingLoaded({
            squadNeeded: true,
          }))
        return Promise.reject(err)
      })
}

const landingLoaded = createAction('LANDING_LOADED')
