import React from 'react'
import { Link } from 'react-router'

function Battle({params}) {
  return <div className="container">
    <h1>Battle {params.id}</h1>
    <Link to="/" className="btn btn-default">I win</Link>
    <Link to="/" className="btn btn-default">I lose</Link>
  </div>
}

module.exports = Battle
