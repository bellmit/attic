import React from 'react'
import { Link } from 'react-router'
import jwtDecode from 'jwt-decode'

export default function LoggedIn({idToken, profile, logout, lock}) {
  return <div className="container">
    <div className="col-md-3">
      <ul>
        <li><Link to="squad">Squad</Link></li>
      </ul>
    </div>
    <div className="col-md-9">
      <p>ID token:</p>
      <pre>{JSON.stringify(jwtDecode(idToken), null, "  ")}</pre>
      <p>Profile:</p>
      {profile &&
        <pre>{JSON.stringify(profile, null, "  ")}</pre>}
      <button className="btn btn-default" onClick={() => logout(lock)}>Log out</button>
    </div>
  </div>
}
