const net = require('net')
const iconv = require('iconv-lite')

function bridgeTcpToSocket(tcp, socket, encoding) {
  tcp.on('close', () => socket.disconnect())
  tcp.on('data', data => {
    const output = iconv.decode(data, encoding)
    socket.emit('output', output)
  })
}

function bridgeSocketToTcp(tcp, socket, encoding) {
  socket.on('disconnect', () => tcp.destroy())
  socket.on('line', line => {
    const input = iconv.encode(line + '\n', encoding)
    tcp.write(input)
  })
}

module.exports = function factory(mooHost, mooPort, mooEncoding) {
  return socket => {
    const tcp = new net.Socket()
    tcp.connect(mooPort, mooHost)

    bridgeTcpToSocket(tcp, socket, mooEncoding)
    bridgeSocketToTcp(tcp, socket, mooEncoding)
  }
}
