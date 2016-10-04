import { actions as modelActions } from './model'
import { bindActionCreators } from 'redux'

export const actions = {
  sendInputLine,
  receiveOutput,
}

export function connect(socket, store) {
  const dispatch = bindActionCreators(actions, store.dispatch)

  socket.on('output', data => dispatch.receiveOutput(data))
}

function sendInputLine(socket, line) {
  return dispatch => {
    dispatch(modelActions.addInputLine(line))
    socket.emit('line', line)
  }
}

function receiveOutput(line) {
  return dispatch => {
    dispatch(modelActions.addOutput(line))
  }
}
