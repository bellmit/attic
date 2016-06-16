'use strict';

function makeBridge(event, nativeEndpoint, socket) {
  return function(...args) {
    nativeEndpoint(...args);
    socket.emit(event, args);
  }
}

const TERMINALS = ['log', 'info', 'warn', 'error'];

/*
 * Forward log messages to terminals on `console` to the Socket.io socket
 * `socket`. This patches each terminal (from TERMINALS) to first call the
 * existing function, so that native browser logging continues uninterrupted
 * even if the socket fails, and then forwards the call over the socket as a
 * `console.TERMINAL` event (`console.log`, etc).
 */
export default function install(console, socket) {
  for (var terminal of TERMINALS) {
    if (console[terminal]) {
      var event = `console.${terminal}`;
      var nativeEndpoint = console[terminal].bind(console);
      console[terminal] = makeBridge(event, nativeEndpoint, socket);
    }
  }
}
