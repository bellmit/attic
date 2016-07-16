'use strict'

import React from 'react'
import { Link } from 'react-router'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import jwtDecode from 'jwt-decode'

import { withApi } from 'app/api'
import { withLock } from 'app/lock/components'
import * as lockActions from 'app/lock/actions'
import * as actions from '../actions'

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
  </div>
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
  </div>
}

const AnonymousLanding = React.createClass({
  componentDidMount() {
    var {login, lock} = this.props
    login(lock, {
      container: "auth0-lock",
    })
  },

  render() {
    return <div className="container">
      <div id="auth0-lock" className="col-md-4"/>
      <div className="col-md-8">
        <LandingCopy />
      </div>
    </div>
  },
})

const LoadingLanding = withApi(React.createClass({
  componentWillMount() {
    var {openSquadIfNeeded, api} = this.props

    openSquadIfNeeded(api)
  },

  render() {
    return false
  },
}))

function Landing({idToken, loading, ...props}) {
  if (!idToken)
    return <AnonymousLanding {...props} />
  if (loading)
    return <LoadingLanding {...props} />
  return <LoggedInLanding idToken={idToken} {...props} />
}

module.exports = connect(
  state => ({
    ...state.lock,
    ...state.landing,
  }),
  dispatch => bindActionCreators({
    ...lockActions,
    ...actions,
  }, dispatch)
)(withLock(Landing))
