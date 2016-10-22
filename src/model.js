import { createAction, handleActions } from 'redux-actions'

const addOutput = createAction('ADD_OUTPUT')
const addInputLine = createAction('ADD_INPUT_LINE')
const changeInput = createAction('CHANGE_INPUT')
const historyPrevious = createAction('HISTORY_PREVIOUS')
const historyNext = createAction('HISTORY_NEXT')

function coalesce(...args) {
  for (const arg of args)
    if (arg !== null)
      return arg
}

function nullIf(candidate, ...values) {
  for (const value of values)
    if (candidate === value)
      return null

  return candidate
}

const output = handleActions({
  [addOutput]: (state, action) => [...state, action.payload],
}, [])

const input = handleActions({
  [addInputLine]: (state, action) => ({
    ...state,
    value: "",
    history: [action.payload, ...state.history],
    historyIndex: null,
  }),
  [historyPrevious]: state => {
    const historyIndex = Math.min(
      coalesce(state.historyIndex, -1) + 1,
      state.history.length - 1
    )
    return {
      ...state,
      historyIndex,
      value: state.history[historyIndex] || state.value,
    }
  },
  [historyNext]: state => {
    const historyIndex = nullIf(
      coalesce(state.historyIndex, 0) - 1,
      -1
    )
    return {
      ...state,
      historyIndex,
      value: state.history[historyIndex] || "",
    }
  },
  [changeInput]: (state, action) => ({
    ...state,
    value: action.payload,
  }),
}, {
  value: "",
  history: [],
  historyIndex: null,
})

export const reducers = {
  output,
  input,
}

export const actions = {
  addOutput,
  addInputLine,
  changeInput,
  historyPrevious,
  historyNext,
}
