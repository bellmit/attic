import React from 'react'
import ReactDOM from 'react-dom'

// This module implements controls for a very simple tabbing system based on a
// pair of properties. NavTab triggers a callback to select a tab by name, and
// highlights the current tab if it is the active tab by name; Tabbed shows or
// hides its children depending on whether the active tab is or is not the
// current tab by name.

// Select a tab.
export function NavTab({active, name, switchTab, children}) {
    return <li
        role="presentation"
        className={ active == name ? 'active' : ''}>
        <a onClick={() => switchTab(name)}>{children}</a>
    </li>
}

// Render the selected tab.
export function Tabbed({active, name, children}) {
    if (active == name) {
        return children
    }
    return null
}
