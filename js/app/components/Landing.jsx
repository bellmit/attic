'use strict';

import React from 'react';
import { Link } from 'react-router';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import jwtDecode from 'jwt-decode';

import { withLock } from 'app/lock/components';
import * as actions from 'app/lock/actions';

function LoggedInLanding({idToken, profile, logout, lock}) {
  return <div className="container">
    <div className="col-md-3">
      <ul>
        <li><Link to="squad">Squad</Link></li>
      </ul>
    </div>
    <div className="col-md-9">
      <p>ID token:</p>
      <pre>{JSON.stringify(jwtDecode(idToken), null, "  ")}</pre>
      <p>Profile:</p>
      {profile &&
        <pre>{JSON.stringify(profile, null, "  ")}</pre>}
      <button className="btn btn-default" onClick={() => logout(lock)}>Log out</button>
    </div>
  </div>;
}

function LandingCopy() {
  return <div className="jumbotron">
    <h1>Distant Shore</h1>
    <p>
      Knausgaard taxidermy gochujang polaroid. Lo-fi truffaut schlitz,
      gluten-free forage pickled biodiesel beard meggings keytar art party
      stumptown slow-carb craft beer.
    </p>
    <p>
      In the finished app, this should be a preview of a live fight, or a
      recording.
    </p>
  </div>;
}

const AnonymousLanding = React.createClass({
  componentDidMount() {
    var {login, lock} = this.props;
    login(lock, {
      container: "auth0-lock",
    });
  },

  render() {
    return <div className="container">
      <div id="auth0-lock" className="col-md-4"/>
      <div className="col-md-8">
        <LandingCopy />
      </div>
    </div>
  },
});

function Landing({idToken, ...props}) {
  if (!idToken)
    return <AnonymousLanding {...props} />
  return <LoggedInLanding idToken={idToken} {...props} />
}

module.exports = connect(
  state => state.lock,
  dispatch => bindActionCreators(actions, dispatch)
)(withLock(Landing));
