import React from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'

import { actions } from './model'

export function App() {
  return <div>
    <Output.connect />
    <Input.connect />
  </div>
}

function Output({lines}) {
  return <div>
    {lines.map((line, index) => <div key={index}>{line}</div>)}
  </div>
}
Output.connect = connect(
  state => ({
    lines: state.output,
  })
)(Output)

function Input({value, changeInput, addInputLine}) {
  return <form onSubmit={event => {
    addInputLine(value);
    event.preventDefault();
  }}>
    <input type="text" onChange={event => changeInput(event.target.value)} value={value} />
  </form>
}
Input.connect = connect(
  state => ({
    value: state.input.value,
  }),
  dispatch => bindActionCreators(actions, dispatch)
)(Input)
