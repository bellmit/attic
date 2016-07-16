import React from 'react'

export function App({name = 'world'}) {
  return <h1>Hello, {name}!</h1>
}

export function NestedApp({name = 'nested'}) {
  return <App name={name} />
}
