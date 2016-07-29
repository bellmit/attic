'use strict'

import React from 'react'
import DocumentTitle from 'react-document-title'

import { connect } from 'react-redux'

function App({booting, children}) {
  return <DocumentTitle title="Distant Shore">
  { !booting &&
    children
  }
  </DocumentTitle>
}

module.exports = connect(
  state => state.lock
)(App)
