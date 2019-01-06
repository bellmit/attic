'use strict'

import React from 'react'

const lockTypes = {
  lock: React.PropTypes.object.isRequired,
}

export const LockProvider = React.createClass({
  childContextTypes: lockTypes,

  getChildContext() {
    return {
      lock: this.props.lock,
    }
  },

  render() {
    return React.Children.only(this.props.children)
  },
})

export function withLock(WrappedComponent, as='lock') {
  return React.createClass({
    displayName: `withLock(${WrappedComponent.displayName})`,
    contextTypes: lockTypes,

    render() {
      var {lock} = this.context
      var lockProps = {
        [as]: lock,
      }
      return <WrappedComponent {...this.props} {...lockProps} />
    },
  })
}
