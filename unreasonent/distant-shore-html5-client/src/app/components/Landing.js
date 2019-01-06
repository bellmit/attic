'use strict'

import React from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import { withLock } from 'app/lock/components'
import * as lockActions from 'app/lock/actions'

import LoggedIn from './Landing/LoggedIn'
import Anonymous from './Landing/Anonymous'

function Landing({idToken, ...props}) {
  if (!idToken)
    return <Anonymous {...props} />
  return <LoggedIn {...props} />
}

module.exports = connect(
  state => ({
    ...state.lock,
    ...state.landing,
  }),
  dispatch => bindActionCreators({
    ...lockActions,
  }, dispatch)
)(withLock(Landing))
