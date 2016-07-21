'use strict'

import subscribe from 'app/subscribe'

describe('subscribe', () => {
  it('combines subscribers by key', () => {
    var lastA, lastB
    function a(state) {
      lastA = state
    }
    function b(state) {
      lastB = state
    }
    var s = subscribe({a, b})

    s({a: 5, b: 8})

    expect(lastA).to.equal(5)
    expect(lastB).to.equal(8)
  })

  it('dedupes calls', () => {
    var calls = 0
    function a() {
      calls++
    }

    var s = subscribe({a})
    s({a: 5})
    expect(calls).to.equal(1)

    s({a: 5})
    expect(calls).to.equal(1)

    s({a: 8})
    expect(calls).to.equal(2)
  })

  it('nests', () => {
    var last
    function b(state) {
      last = state
    }

    var s = subscribe({
      a: subscribe({b}),
    })

    s({
      a: {
        b: 37,
      },
    })

    expect(last).to.equal(37)
  })

  it('flattens undefined states', () => {
    var last = 'SENTINEL'
    function a(state) {
      last = state
    }
    var s = subscribe({a})

    s(undefined)

    expect(last).to.be.undefined
  })

  it('flattens nested undefined states', () => {
    var last = 'SENTINEL'
    function b(state) {
      last = state
    }
    var s = subscribe({
      a: subscribe({b}),
    })

    s(undefined)

    expect(last).to.be.undefined
  })

})
