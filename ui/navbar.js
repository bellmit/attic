import React from 'react'
import ReactDOM from 'react-dom'
import { Route, Link } from 'react-router-dom'

import Documents, * as documents from './documents'
import Doc, * as doc from './document'

// Renders a link appropriate for use within a top-level nav bar, and ensures
// that only the correct link is given the active class (for highlighting).
function NavLink({path, to, children}) {
    return <li className={path == to ? "active" : ""}>
        <Link to={to}>{children}</Link>
    </li>
}

// Renders a Bootstrap navigation bar for the application. This navbar includes
// contextual elements for other application views (specifically, the documents
// and document editor views), and provides top-level navigation between the
// app's core features.
export default function NavBar({location}) {
    return <nav className="navbar navbar-default">
        <div className="container-fluid">
            <ul className="nav navbar-nav">
                <NavLink path={location.pathname} to='/'>Documents</NavLink>
                <NavLink path={location.pathname} to='/state'>Browse State</NavLink>
            </ul>
            <Route exact path="/" component={documents.Navbar.connect} />
            <Route exact path="/document/:messageId" component={doc.Navbar.connect} />
        </div>
    </nav>
}
