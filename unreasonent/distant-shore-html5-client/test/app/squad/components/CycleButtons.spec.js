import React from 'react'
import { mount } from 'enzyme'

import CycleButtons from 'app/squad/components/CycleButtons'

describe('CycleButtons', function () {
  beforeEach(function () {
    var options = ['A', 'B', 'C']
    var option = 'A'

    this.onSelect = chai.spy()
    this.wrapper = mount(
      <CycleButtons
        options={options}
        value={option}
        onSelect={this.onSelect} />
    )
    this.buttons = this.wrapper.find('button')
  })

  it('renders a button group with left and right buttons', function () {
    expect(this.wrapper).to.have.className('btn-group')

    expect(this.buttons).to.have.length(2)
    this.buttons.forEach(button => {
      expect(button).to.have.className('btn')
      expect(button).to.have.className('btn-default')
    })
    expect(this.buttons.at(0)).to.have.className('btn-prev')
    expect(this.buttons.at(0)).to.have.text('<')
    expect(this.buttons.at(1)).to.have.className('btn-next')
    expect(this.buttons.at(1)).to.have.text('>')
  })

  it('returns the next value when a user clicks the > button', function () {
    this.buttons.find('.btn-next').simulate('click')
    expect(this.onSelect).to.have.been.called.with('B')
  })

  it('returns the next value when a user clicks the > button, wrapping as needed', function () {
    var options = ['A', 'B', 'C']
    var option = 'C'

    var wrapper = mount(
      <CycleButtons
        options={options}
        value={option}
        onSelect={this.onSelect} />
    )
    var buttons = wrapper.find('button')

    buttons.find('.btn-next').simulate('click')
    expect(this.onSelect).to.have.been.called.with('A')
  })

  it('returns the previous value when a user clicks the < button, wrapping as needed', function () {
    this.buttons.find('.btn-prev').simulate('click')
    expect(this.onSelect).to.have.been.called.with('C')
  })
})
