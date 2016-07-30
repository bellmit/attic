import React from 'react'
import { Link } from 'react-router'
import Copy from './Copy'

export default function LoggedIn({logout, lock}) {
  return <div className="container">
    <div className="list-group col-md-4">
      <Link to="squad" className="list-group-item">
        <h2 className="list-group-item-heading">Edit your squad</h2>
        <p className="list-group-item-text">
          Rebuild your squad and come back stronger than ever.
        </p>
      </Link>
      <Link className="list-group-item" to="/challenge/123">
        <h2 className="list-group-item-heading">Challenge a friend</h2>
        <p className="list-group-item-text">
          DIY normcore wayfarers godard, truffaut cold-pressed occupy forage.
        </p>
      </Link>
      <Link className="list-group-item" to="/ranked">
        <h2 className="list-group-item-heading">Ranked battle</h2>
        <p className="list-group-item-text">
          Take on challengers to prove you're the best there is.
        </p>
      </Link>
      <Link className="list-group-item" to="/battle/123">
        <h2 className="list-group-item-heading">Practice battle</h2>
        <p className="list-group-item-text">
          Protect the land from rampaging monsters and practice with your squad.
        </p>
      </Link>
      <button className="list-group-item" onClick={() => logout(lock)}>
        <h2 className="list-group-item-heading">Log out</h2>
        <p className="list-group-item-text">
          Protect your account when playing at internet cafés or on shared computers.
        </p>
      </button>
    </div>
    <div className="col-md-8">
      <Copy />
    </div>
  </div>
}
