import React from 'react'
import ReactDOM from 'react-dom'
import { withRouter } from 'react-router'
import { Route } from 'react-router-dom'

import Documents from './documents'
import Doc from './document'
import State from './state'
import Login from './login'

import NavBar from './navbar'

// The application's root view. This renders within a top-level `div` on the
// final HTML page, and renders whichever primary view is appropriate for the
// current route.
//
// It also renders a navigation bar.
export default function App({location}) {
    return <div>
        <NavBar location={location} />
        <Route exact path="/" component={Documents.connect} />
        <Route exact path="/document/:messageId" component={Doc.connect} />
        <Route exact path="/state" component={State.connect} />
        <Login.withApi />
    </div>
}

// As above, but with the properties from the current router injected. This
// spares callers from having to specify the `location` property: it's inferred
// from the current route, instead.
App.routed = withRouter(App)
