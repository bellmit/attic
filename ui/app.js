import React from 'react'
import ReactDOM from 'react-dom'
import { HashRouter } from 'react-router-dom'
import { Provider } from 'react-redux'

import App from './root'
import createStore from './store'
import { ApiProvider } from './api'

// The Cadastre web interface is a React.js (<https://react.js.org>)
// application, in spite of my misgivings about the licensing situation. This
// entry point assembles the root React components and binds them to the page,
// then hands off to React to handle rendering and interaction from there.
//
// Cadastre relies on the Redux framework (<https://redux.js.org>) to manage UI
// state, rather than relying directly on per-component internal state. During
// the bootstrap process, this module creates an initial Redux store holding the
// UI's initial state, then renders the initial view on the page's `app` DOM
// node using that store.
//
// From that point onwards, all further interaction is handled by one of those
// two frameworks: Redux processes state changes and sets the properties of
// React components, and React renders those components into the DOM.

const store = createStore()

// Render the root of the application:
//
// * Inject the Redux store and an API client instance into the entire
//   application, uniformly.
//
// * Route the whole app. The application views have unique URLs which can
//   safely be reloaded. This code uses `HashRouter` (and generates path-like
//   URL fragment identifiers) to limit the amount of additional code required
//   on the service side.
//
// * Render a route-aware version of the app's root element.

ReactDOM.render(
    <Provider store={store}>
        <ApiProvider>
            <HashRouter>
                <App.routed />
            </HashRouter>
        </ApiProvider>
    </Provider>,
    document.getElementById('app'),
)
