import React from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { actions as modelActions } from './model'
import { actions as relayActions } from './relay'

export function App({relay}) {
  return <div>
    <Output.connect />
    <Input.connect relay={relay} />
  </div>
}

function Output({lines}) {
  return <div>
    {/* `lines` is a sequence of telnet chunks - basically raw TCP messages,
       broken up at arbitrary boundaries by the needs of the underlying network.
       Bolt it together into a single giant stream for rendering, until we have
       something smarter. */}
    {lines.join('')}
  </div>
}
Output.connect = connect(
  state => ({
    lines: state.output,
  })
)(Output)

function Input({relay, value, changeInput, sendInputLine}) {
  return <form onSubmit={event => {
    event.preventDefault()
    sendInputLine(relay, value)
  }}>
    <input
      type="text"
      onChange={event => changeInput(event.target.value)}
      value={value} />
  </form>
}
Input.connect = connect(
  state => ({
    value: state.input.value,
  }),
  dispatch => bindActionCreators({
    ...modelActions,
    ...relayActions,
  }, dispatch)
)(Input)
