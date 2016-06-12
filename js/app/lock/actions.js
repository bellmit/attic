'use strict';

import { createAction } from 'redux-actions';
import { replace } from 'react-router-redux';
import Auth0Lock from 'auth0-lock';

import whoAmI from 'app/api/who-am-i';

const lock = new Auth0Lock(
  appConfig.AUTH0_CLIENT_ID,
  appConfig.AUTH0_DOMAIN
);

function getProfile(idToken) {
  return new Promise((resolve, reject) => {
    lock.getProfile(idToken, (err, profile) => {
      if (err)
        reject(err);
      else
        resolve(profile);
    });
  });
}

function bootFromToken(idToken) {
  return dispatch => {
    Promise.all([getProfile(idToken), whoAmI(idToken)])
      .then(([profile, apiIdentity]) => dispatch(lockSuccess({
        ...apiIdentity,
        idToken,
        profile,
      })));
  };
}

function bootFromHash(hash) {
  return dispatch => {
    dispatch(replace({
      // Restore mid-app URLs (see login() action for the source of this data)
      path: hash.state,
      // Get the token out of the URL, irreversibly (browser `back` won't
      // restore it)
      hash: null,
    }));

    if (hash.error) {
      console.log("Login hash error:", hash);
      return;
    }

    var idToken = hash.id_token;
    dispatch(bootFromToken(idToken));
  };
}

export function boot() {
  return dispatch => {
    var hash = lock.parseHash();
    if (!hash)
      return;

    dispatch(bootFromHash(hash));
  };
}

export function login() {
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
      socialBigButtons: true,
      // Force the use of JWT even though we're providing a callback URL.
      responseType: 'token',
      // Always redirect back to app root ...
      callbackURL: window.location.origin,
      authParams: {
        // ... but include enough info for boot() to restore path.
        state: window.location.pathname,
      },
    });
  };
}

export function logout() {
  return dispatch => {
    dispatch(lockClear());
    lock.logout({
      client_id: appConfig.AUTH0_CLIENT_ID,
      returnTo: window.location.origin,
    });
  }
}

export const lockSuccess = createAction('LOCK_SUCCESS');

export const lockClear = createAction('LOCK_CLEAR');
