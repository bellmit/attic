import { createAction, handleActions } from 'redux-actions'

const addOutput = createAction('ADD_OUTPUT')
const addInputLine = createAction('ADD_INPUT_LINE')
const changeInput = createAction('CHANGE_INPUT')

const output = handleActions({
  [addOutput]: (state, action) => [...state, action.payload],
}, [])

const input = handleActions({
  [addInputLine]: (state, action) => ({
    ...state,
    value: "",
    history: [...state.history, action.payload],
  }),
  [changeInput]: (state, action) => ({
    ...state,
    value: action.payload,
  }),
}, {
  value: "",
  history: [],
})

export const reducers = {
  output,
  input,
}

export const actions = {
  addOutput,
  addInputLine,
  changeInput,
}
