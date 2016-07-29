import React from 'react'

import Copy from './Copy'

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
        <Copy />
      </div>
    </div>
  },
})

export default AnonymousLanding
