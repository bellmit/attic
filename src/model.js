import { createAction, handleActions } from 'redux-actions'

const addOutputLine = createAction('ADD_OUTPUT_LINE')
const addInputLine = createAction('ADD_INPUT_LINE')
const changeInput = createAction('CHANGE_INPUT')

const output = handleActions({
  [addOutputLine]: (state, action) => [...state, action.payload],
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
  addOutputLine,
  addInputLine,
  changeInput,
}
