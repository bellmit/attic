'use strict'

import keys from 'lodash.keys'

function keySubscriber(key, callback) {
  var last = undefined
  return state => {
    var next = state === undefined ? undefined : state[key]
    if (last === undefined || last !== next) {
      last = next
      callback(next)
    }
  }
}

function reduceSubscribers(first, next) {
  return (...args) => {
    first(...args)
    next(...args)
  }
}

export default function subscribe(subscribers) {
  return keys(subscribers)
    .map(key => keySubscriber(key, subscribers[key]))
    .reduce(reduceSubscribers)
}

export function mount(store, subscriber) {
  function listener() {
    var state = store.getState()
    subscriber(state)
  }
  return store.subscribe(listener)
}
