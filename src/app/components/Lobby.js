import React from 'react'

import { Link } from 'react-router'

export default function Lobby(props) {
  return <div {...props}>
    <Link to="/battle/123" className="btn btn-default">Challenge a friend</Link>
  </div>
}
