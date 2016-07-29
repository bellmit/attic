import React from 'react'
import { Link } from 'react-router'

export default function LoggedIn({logout, lock}) {
  return <div className="container">
    <div className="col-md-3">
      <ul>
        <li><Link to="squad">Squad</Link></li>
      </ul>
    </div>
    <div className="col-md-9">
      <button className="btn btn-default" onClick={() => logout(lock)}>Log out</button>
    </div>
  </div>
}
