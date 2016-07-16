import React from 'react'
import { mount } from 'enzyme'

import { App } from 'components'

describe('App', () => {
  it('uses an h1', () => {
    const wrapper = mount(<App name="bob" />)
    expect(wrapper).to.have.tagName('h1')
  })

  it('renders a greeting', () => {
    const wrapper = mount(<App name="bob" />)
    expect(wrapper).to.have.text("Hello, bob!")
  })
})
