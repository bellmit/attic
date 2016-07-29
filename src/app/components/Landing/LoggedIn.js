import React from 'react'
import { Link } from 'react-router'

export default function LoggedIn({logout, lock}) {
  return <div className="container">
    <div className="col-md-3">
      <ul>
        <li><Link to="squad">Squad</Link></li>
        <li><a role="button" onClick={() => logout(lock)}>Log out</a></li>
      </ul>
    </div>
    <div className="col-md-9">
    </div>
  </div>
}
