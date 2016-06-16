'use strict';

import domReady from 'detect-dom-ready';
import asset from './app/asset';
import io from 'socket.io-client';
import logging from './app/logging';

var socket = io();
logging(console, socket);

domReady(() => {
  // use explicit module index as a concession to Mac OS developers. Stops
  // "require('./app')" from resolving to this very file.
  require('./app/index');
});
