import React from 'react'
import { Link } from 'react-router'
import Lobby from 'app/components/Lobby'

export default function LoggedIn({logout, lock}) {
  return <div className="container">
    <h1>Play Now</h1>
    <div className="col-md-3">
      <ul>
        <li><Link to="squad">Squad</Link></li>
        <li><a role="button" onClick={() => logout(lock)}>Log out</a></li>
      </ul>
    </div>
    <Lobby className="col-md-9" />
  </div>
}
