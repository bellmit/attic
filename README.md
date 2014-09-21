# Node Server End-to-End Testing Example

## To run tests

Tests run using [jasmine-node](https://www.npmjs.org/package/jasmine-node) and can be invoked using NPM:

    npm test

## To run the server

The server can be run outside of the tests, and will listen on http://localhost:5000/. It supports one endpoint (`/greet`) that provides a Hello World Web Service. Fancy. You can start it with `node server.js` or with NPM:

    npm start

## Layout

* Tests in `test/server`
* Server itself in `server.js`
* Server-side modules in `server/`
* Server deps in `dependencies`, testing-only deps in `devDependencies` to keep them out of the server's module loader under normal operation.

## Principles

* The tests operate the server the same way a client or browser would: via HTTP. Therefore, the tests start and stop a server as needed, using the same startup you'd use to run the server by hand.

* When running tests, we need the tests to start only after the server is ready to accept requests. Therefore, the server cooperates. Using `child_process.fork` to launch the server gives the server a `process.send` method it can use to notify the tests; we pass a "started" message back to the tests once the server is accepting requests. Otherwise, when the server runs standalone, we don't; there's nobody there to receive it.

    This avoids the case where tests start trying to send requests before the server is fully up. This happens _often_ -- nearly every test run fails this specific way.