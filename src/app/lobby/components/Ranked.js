import React from 'react'
import { Link } from 'react-router'

module.exports = function Ranked() {
  return <div className="container">
    <h1>Finding a challenger</h1>
    <p>I dunno some loading-screen copy</p>
    <hr />
    <ul>
      <li><Link to="/battle/123">move ahead to battle</Link></li>
      <li><Link to="/">abort and go back to landing</Link></li>
    </ul>
  </div>
}
