'use strict';

import React from 'react';
import { Link } from 'react-router';

function Landing() {
  return <ul>
    <li><Link to="squad">Squad</Link></li>
  </ul>;
}

module.exports = Landing;
