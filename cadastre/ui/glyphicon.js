import React from 'react'
import ReactDOM from 'react-dom'

// Renders a Bootstrap Glyphicon icon in an empty `span` element.

export default function Glyphicon({name}) {
    return <span className={`glyphicon glyphicon-${name}`} aria-hidden="true" />
}
