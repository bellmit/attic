import React from 'react'
import { mount } from 'enzyme'

import { Output, Input } from '../src/components'

describe('Output', () => {
  it('renders empty output', () => {
    const wrapper = mount(
      <Output
        lines={[]} />
    )

    expect(wrapper).to.have.text('')
  })

  it('renders a line', () => {
    const wrapper = mount(
      <Output
        lines={['A line']} />
    )

    expect(wrapper).to.have.text('A line')
  })

  it('joins fragments', () => {
    const wrapper = mount(
      <Output
        lines={['A', ' line']} />
    )

    expect(wrapper).to.have.text('A line')
  })
})

describe('Input', () => {
//export function Input({relay, value, changeInput, sendInputLine, historyPrevious, historyNext}) {
  beforeEach(function() {
    this.relay = chai.spy()
    this.changeInput = chai.spy()
    this.sendInputLine = chai.spy()
    this.historyPrevious = chai.spy()
    this.historyNext = chai.spy()

    this.wrapper = mount(
      <Input
        relay={this.relay}
        value={'value'}
        changeInput={this.changeInput}
        sendInputLine={this.sendInputLine}
        historyPrevious={this.historyPrevious}
        historyNext={this.historyNext} />
    )
  })

  it('renders pending input', function() {
    expect(this.wrapper.find('input')).to.have.value('value')
  })

  it('transmits input changes', function() {
    this.wrapper.find('input').simulate('change')
    expect(this.changeInput).to.have.been.called.with('value')
  })

  it('sends input lines', function() {
    this.wrapper.simulate('submit')
    expect(this.sendInputLine).to.have.been.called.with(this.relay, 'value')
  })

  it('signals history scrollback', function() {
    this.wrapper.find('input').simulate('keyup', { key: "ArrowUp" })
    expect(this.historyPrevious).to.have.been.called()
  })

  it('signals history scrollforward', function() {
    this.wrapper.find('input').simulate('keyup', { key: "ArrowDown" })
    expect(this.historyNext).to.have.been.called()
  })
})
