'use strict';

import React from 'react';
import { Link } from 'react-router';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import jwtDecode from 'jwt-decode';
import * as actions from 'app/lock/actions';

function Landing({idToken, profile, login, logout}) {
  return <div className="container">
    <div className="col-md-3">
      <ul>
        <li><Link to="squad">Squad</Link></li>
      </ul>
    </div>
    <div className="col-md-9">
      <p>ID token: {idToken}</p>
      {idToken &&
        <pre>{JSON.stringify(jwtDecode(idToken), null, "  ")}</pre>
      }
      <p>Profile:</p>
      {profile &&
        <pre>{JSON.stringify(profile, null, "  ")}</pre>}
      {idToken ?
        <button className="btn btn-default" onClick={logout}>Log out</button> :
        <button className="btn btn-default" onClick={login}>Log in</button>
      }
    </div>
  </div>;
}

module.exports = connect(
  state => state.lock,
  dispatch => bindActionCreators(actions, dispatch)
)(Landing);
