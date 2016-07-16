'use strict'

import { createAction } from 'redux-actions'
import { replace } from 'react-router-redux'
import jwtDecode from 'jwt-decode'

function getProfile(lock, idToken) {
  return new Promise((resolve, reject) => {
    lock.getProfile(idToken, (err, profile) => {
      if (err)
        reject(err)
      else
        resolve(profile)
    })
  })
}

function refreshToken(dispatch, getState, api, lock) {
  var state = getState()
  var idToken = state.lock.idToken
  if (idToken) {
    lock.getClient().renewIdToken(idToken, (err, result) => {
      if (err) {
        console.error('lock.refresh.failed', err)
        dispatch(abandon())
      }
      else
        dispatch(bootFromToken(result.id_token, api, lock))
    })
  }
}

function tokenExpires(idToken) {
  var claims = jwtDecode(idToken)
  if (claims.exp) {
    var now = Date.now() // epoch millis
    var exp = claims.exp * 1000 // epoch sec -> epoch millis
    var remaining = exp - now // worst case: already invalid.
    return remaining
  }
  return null
}

/*
 * This is the primary entry point for applying a new token. This function will
 * discard expired tokens (which handles the case that we have a cached token,
 * but it has expired), then otherwise store the token to window.localStorage,
 * schedule a token refresh and find our profile from Auth0.
 *
 * We can get here a few ways:
 *
 * - from bootFromHash, with a fresh token from a completed login attempt
 * - from boot, with a token from window.localStorage
 * - from refreshToken, with a token obtained from auth0's renewal API
 */
function bootFromToken(idToken, api, lock) {
  return (dispatch, getState) => {
    var remaining = tokenExpires(idToken)
    if (remaining && remaining <= 0) // too late, this one's dead. Give up on it.
      return dispatch(abandon())

    window.localStorage.idToken = idToken
    dispatch(lockSuccess({
      idToken,
    }))

    if (remaining) { // will expire, will need to refresh it in half the remaining lifetime.
      var refreshIn = remaining / 2 // relative millis
      setTimeout(refreshToken, refreshIn, dispatch, getState, api, lock)
    }

    getProfile(lock, idToken)
      .then(profile => dispatch(lockSuccess({
        profile,
      })))
  }
}

function bootFromHash(hash, api, lock) {
  return dispatch => {
    dispatch(replace({
      // Restore mid-app URLs (see login() action for the source of this data)
      path: hash.state,
      // Get the token out of the URL, irreversibly (browser `back` won't
      // restore it)
      hash: null,
    }))

    if (hash.error) {
      console.log('lock.hash.failed', hash.error)
      return
    }

    var idToken = hash.id_token
    dispatch(bootFromToken(idToken, api, lock))
  }
}

function bootAnonymously() {
  return lockSuccess({})
}

/*
 * Bootstrap the lock:
 *
 * - On fresh page views, remain logged out.
 * - On return from auth0, apply the newly-obtained token to the lock.
 * - On return with an existing token, apply it to the lock.
 * - Schedule automatic token refresh, if necessary.
 */
export function boot(api, lock) {
  return dispatch => {
    var hash = lock.parseHash()
    if (hash) {
      dispatch(bootFromHash(hash, api, lock))
      return
    }

    var idToken = window.localStorage.idToken
    if (idToken) {
      dispatch(bootFromToken(idToken, api, lock))
      return
    }

    dispatch(bootAnonymously())
  }
}

export function login(lock, options) {
  return dispatch => {
    /*
     * So. We have to use an explicit callback URL here, because we use a
     * browserHistory for routing URLs but Auth0 requires a page-by-page
     * whitelist of callback URLs. Rather than trying to catalogue the entire
     * app, we tell Auth0 to return to the root URL, and smuggle the actual app
     * path back through the 'state' parameter.
     *
     * Howver, when provided with an explicit callbackURL, Auth0 presumes we
     * want OAuth-style negotiation, not JWT. We force it back to JWT, since
     * that's what this client uses; we're not a complete OAuth implementation.
     */
    lock.show({
      ...options,
      socialBigButtons: true,
      // Force the use of JWT even though we're providing a callback URL.
      responseType: 'token',
      // Always redirect back to app root ...
      callbackURL: window.location.origin,
      authParams: {
        // ... but include enough info for boot() to restore path.
        state: window.location.pathname,
      },
    })
  }
}

function abandon() {
  return dispatch => {
    delete window.localStorage.idToken
    dispatch(lockClear())
  }
}

export function logout(lock) {
  return dispatch => {
    dispatch(abandon())
    lock.logout({
      client_id: appConfig.AUTH0_CLIENT_ID,
      returnTo: window.location.origin,
    })
  }
}

const lockSuccess = createAction('LOCK_SUCCESS')

const lockClear = createAction('LOCK_CLEAR')
