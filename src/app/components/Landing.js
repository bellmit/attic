'use strict'

import React from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import { withLock } from 'app/lock/components'
import * as lockActions from 'app/lock/actions'
import * as actions from '../actions'

import LoggedIn from './Landing/LoggedIn'
import Anonymous from './Landing/Anonymous'
import Loading from './Landing/Loading'

function Landing({idToken, loading, ...props}) {
  if (!idToken)
    return <Anonymous {...props} />
  if (loading)
    return <Loading {...props} />
  return <LoggedIn idToken={idToken} {...props} />
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
