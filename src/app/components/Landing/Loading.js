import React from 'react'
import { withApi } from 'app/api'

const Loading = withApi(React.createClass({
  componentWillMount() {
    var {openSquadIfNeeded, api} = this.props

    openSquadIfNeeded(api)
  },

  render() {
    return false
  },
}))

export default Loading
