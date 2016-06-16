'use strict';

function makeBridge(endpoint, tags) {
  return function(message) {
    if (!Array.isArray(message))
      message = [message];
    var [first, ...rest] = message;
    endpoint(first, ...tags, ...rest);
  }
}

function connected(socket) {
  var id = socket.id;
  var address = socket.handshake.address;
  var tags = [`client=${address}`, `socket=${id}`];

  console.log('logging.connected', ...tags);

  var terminals = ['log', 'info', 'warn', 'error'];
  for (var terminal of terminals) {
    var nativeEndpoint = console[terminal].bind(console);
    socket.on(`console.${terminal}`, makeBridge(nativeEndpoint, tags));
  }

  socket.on('disconnect', function() {
    console.log('logging.disconnected', ...tags);
  });
}

module.exports = function install(io) {
  io.on('connection', connected);
}
