'use strict'

import * as sg from 'app/squad/string-generator'

describe('a string generator', () => {
  it('requires at least one rule', () => {
    var generator = sg.generator()

    expect(() => generator.generate('example')).to.throw(Error)
  })

  it('returns a single-template literal rule unchanged', () => {
    var generator = sg.generator()
    generator.addRule('example', "Example String")
    
    expect(generator.generate('example')).to.equal("Example String")
  })

  it('expands inline templates', () => {
    var generator = sg.generator()
    generator.addRule('example', "Outer {inner}")
    generator.addRule('inner', "Inner")

    expect(generator.generate('example')).to.equal("Outer Inner")
  })

  it('loads json trees', () => {
    var source = {
      rules: {
        "example": ["example {rule}"],
        "rule": ["from json"],
      },
    }
    var generator = sg.load(source)
    expect(generator.generate('example')).to.equal("example from json")
  })
})
