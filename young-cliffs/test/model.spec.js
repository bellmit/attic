import { createAction } from 'redux-actions'

import { reducers, actions } from '../src/model'

const initAction = createAction('INIT')

describe('output', () => {
  it('is initially empty', () => {
    const state = reducers.output(undefined, initAction())
    expect(state).to.be.empty
  })

  it('appends output chunks as they arrive', () => {
    const state = reducers.output([], actions.addOutput('foo'))
    expect(state).to.eql(['foo'])
  })

  it('appends multiple output chunks', () => {
    const stateA = reducers.output([], actions.addOutput('foo'))
    const state = reducers.output(stateA, actions.addOutput('bar'))
    expect(state).to.eql(['foo', 'bar'])
  })
})

describe('input', () => {
  describe('initial state', () => {
    beforeEach(function() {
      this.state = reducers.input(undefined, initAction())
    })

    it('has an empty value', function() {
      expect(this.state.value).to.equal('')
    })

    it('has an empty history', function() {
      expect(this.state.history).to.be.empty
    })

    it('has no history index', function() {
      expect(this.state.historyIndex).to.be.null
    })
  })

  it('updates the value', () => {
    const state = reducers.input(undefined, actions.changeInput('foo'))
    expect(state.value).to.equal('foo')
  })

  it('blanks the value when adding a new input line', () => {
    const stateA = reducers.input(undefined, actions.changeInput('foo'))
    const state = reducers.input(stateA, actions.addInputLine('an input line'))

    expect(state.value).to.equal('')
  })

  describe('history tracking', () => {
    it('adds newly entered lines to history', () => {
      const state = reducers.input(undefined, actions.addInputLine('an input value'))

      expect(state.history).to.eql(['an input value'])
    })

    it('adds newly entered lines to history at the front', () => {
      const stateA = reducers.input(undefined, actions.addInputLine('an input value'))
      const state = reducers.input(stateA, actions.addInputLine('a second input value'))

      expect(state.history).to.eql(['a second input value', 'an input value'])
    })

    it('fills in the previous line', () => {
      const stateA = reducers.input(undefined, actions.addInputLine('an input value'))
      const stateB = reducers.input(stateA, actions.addInputLine('a second input value'))
      const state = reducers.input(stateB, actions.historyPrevious())

      expect(state.value).to.equal('a second input value')
    })

    it('fills in the next-to-previous line', () => {
      const stateA = reducers.input(undefined, actions.addInputLine('an input value'))
      const stateB = reducers.input(stateA, actions.addInputLine('a second input value'))
      const stateC = reducers.input(stateB, actions.historyPrevious())
      const state = reducers.input(stateC, actions.historyPrevious())

      expect(state.value).to.equal('an input value')
    })

    it('stops at the oldest line', () => {
      const stateA = reducers.input(undefined, actions.addInputLine('an input value'))
      const stateB = reducers.input(stateA, actions.addInputLine('a second input value'))
      const stateC = reducers.input(stateB, actions.historyPrevious())
      const stateD = reducers.input(stateC, actions.historyPrevious())
      const state = reducers.input(stateD, actions.historyPrevious())

      expect(state.value).to.equal('an input value')
    })

    it('fills in the next line', () => {
      const stateA = reducers.input(undefined, actions.addInputLine('an input value'))
      const stateB = reducers.input(stateA, actions.addInputLine('a second input value'))
      const stateC = reducers.input(stateB, actions.historyPrevious())
      const stateD = reducers.input(stateC, actions.historyPrevious())
      const stateE = reducers.input(stateD, actions.historyPrevious())
      const state = reducers.input(stateE, actions.historyNext())

      expect(state.value).to.equal('a second input value')
    })

    it('blanks at the start of history', () => {
      const stateA = reducers.input(undefined, actions.addInputLine('an input value'))
      const stateB = reducers.input(stateA, actions.addInputLine('a second input value'))
      const stateC = reducers.input(stateB, actions.historyPrevious())
      const stateD = reducers.input(stateC, actions.historyPrevious())
      const stateE = reducers.input(stateD, actions.historyPrevious())
      const stateF = reducers.input(stateE, actions.historyNext())
      const state = reducers.input(stateF, actions.historyNext())

      expect(state.value).to.equal('')
    })
  })
})
