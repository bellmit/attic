import { App, NestedApp } from './components'

export const routes = [
  {
    path: '/nested',
    component: NestedApp,
  },
  {
    path: '/',
    component: App,
  },
]
