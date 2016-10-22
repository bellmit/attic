# Young Cliffs

## Overview

This project provides an HTML5 client for telnet-based MUD servers, primarily LambdaMOO-based servers. The overall architecture is broken into three parts:

* An HTML5 view, written largely in React,
* A NodeJS relay server, and
* The MOO server itself.

As HTML5 apps can only speak browser-supported protocols, the browser cannot connect directly to a MOO server. The relay server acts as an adapter:

```
[ HTML5 view ] <-- socket.io connection --> [ relay server ] <-- Telnet --> [ actual MOO Server ]
```

The relay server is relatively dumb: the only service it provides, besides acting as a dumb asset server for the HTML5 view, is to transcode and relay bytes to and from the MOO server. Incoming commands from the HTML5 view are converted to Telnet messages (basically, raw ASCII strings, with a `\r\n` terminator) and sent onwards. Outgoing messages from the MOO server are converted back to text as-is, including embedded line breaks, and forwarded back to the HTML5 view over a socket.io connection for rendering.

Any failure of either the socket.io connection or the telnet connection for a session resets the whole session. As the relay server is stateful, it also reacts badly to being clustered or load-balanced.

## Languages

This project is broken up into two subtrees:

* `src` contains the HTML5 view. This is a React project, using ES2016 + JSX for all sources. An included Webpack configuration handles conversion to Javascript and packaging for the browser; the delivered code includes comprehensive source maps to allow in-browser debugging and exploration.

    * This uses ES2016-style `import` statements. To reduce the code bulk of the underlying frameworks, I've used Webpack 2's dead-dependency removal and Uglify to remove unimported methods and symbols from the final artifact. These features rely on static analysis of the source code.

* `relay` contains the Node-based relay server. This is a bare Node project, and can be run as-is under Node 6 or later.

    * This uses CommonJS-style `require()` function calls for dependencies. Node, as of this writing, does not support `import` statements.

## Build

`npm install` will download dependencies, then build the app using Webpack.

Alternately:

```
npm run webpack       # Run webpack once
npm run webpack-watch # Continuously run as sources change
```

Setting `NODE_ENV=development` will disable minification and put React into development mode.

## Testing

`npm test` will run Karma tests.

Alternately:

```
npm run karma       # Run karma once
npm run karma-watch # Run karma continuously as sources change
```

## Running Locally

`bin/web` will run the web server on port 4000.

## Configuration

The relay server (`bin/web`) respects the following environment variables:

* `PORT`: HTTP listen port. Defaults to port 4000 if not set.
* `MOO_HOST`: Server hostname or IP address for the MOO served by this app.
* `MOO_PORT`: Server port number for the MOO served by this app.
* `MOO_ENCODING`: Text encoding to use when communicating with the MOO server. This defaults to `Latin-1` if not set, as the stock LambdaMOO codebase speaks ASCII plus Telnet escape sequences.
