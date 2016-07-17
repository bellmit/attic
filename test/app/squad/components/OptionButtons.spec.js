import React from 'react'
import { mount } from 'enzyme'

import OptionButtons from 'app/squad/components/OptionButtons'

describe('OptionButtons', () => {
  describe('using an array of options', () => {
    var options = ['A', 'B']
    var option = 'A'
    var onSelect = chai.spy()
    var wrapper = mount(
      <OptionButtons
        options={options}
        value={option}
        onSelect={onSelect} />
    )
    var buttons = wrapper.find('button')

    it('renders a button group with two buttons', () => {
      expect(wrapper).to.have.className('btn-group')

      expect(buttons).to.have.length(2)
      buttons.forEach(button => {
        expect(button).to.have.className('btn')
        expect(button).to.have.className('btn-default')
      })
    })

    it('renders buttons labelled with the provided options', () => {
      expect(buttons.at(0)).to.have.text('A')
      expect(buttons.at(1)).to.have.text('B')
    })

    it('highlights the active button', () => {
      expect(buttons.at(0)).to.have.className('active')
      expect(buttons.at(1)).not.to.have.className('active')
    })

    it('passes the clicked value back', () => {
      buttons.at(1).simulate('click')
      expect(onSelect).to.have.been.called.with('B')
    })
  })

  describe('using an options object', () => {
    var options = {
      'A': 'Label A',
      'B': <span id='label-b' />,
    }
    var option = 'A'
    var onSelect = chai.spy()
    var wrapper = mount(
      <OptionButtons
        options={options}
        value={option}
        onSelect={onSelect} />
    )
    var buttons = wrapper.find('button')

    it('renders a button group with two buttons', () => {
      expect(wrapper).to.have.className('btn-group')

      expect(buttons).to.have.length(2)
      buttons.forEach(button => {
        expect(button).to.have.className('btn')
        expect(button).to.have.className('btn-default')
      })
    })

    it('renders buttons labelled with the provided options', () => {
      expect(buttons.at(0)).to.have.text('Label A')
      expect(buttons.at(1)).to.contain(<span id='label-b' />)
    })

    it('highlights the active button', () => {
      expect(buttons.at(0)).to.have.className('active')
      expect(buttons.at(1)).not.to.have.className('active')
    })

    it('passes the clicked value back', () => {
      buttons.at(1).simulate('click')
      expect(onSelect).to.have.been.called.with('B')
    })
  })

  describe('with buttonClassName', () => {
    var options = ['A', 'B']
    var wrapper = mount(
      <OptionButtons
        options={options}
        buttonClassName='btn-sample' />
    )
    var buttons = wrapper.find('button')

    it('renders buttons that have the additional classes', () => {
      expect(buttons).to.have.length(2)
      buttons.forEach(button => {
        expect(button).to.have.className('btn')
        expect(button).to.have.className('btn-default')
        expect(button).to.have.className('btn-sample')
      })
    })
  })
})
