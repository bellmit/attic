# Web Chat Example

A small chat example using WebSockets to pass JSON between browsers running
Javascript, as mediated by a small web server written in Node.

To understand how this software works, read the comments in `server.js` in this
directory.

## You Will Need

* A text editor
* A web browser
* A terminal set up the way you like it
* A copy of this source tree

You will also need the following versions:

| Software | Version         | `brew` installation instructions |
| -------- | --------------- | -------------------------------- |
| NodeJS   | 12.9.0 or later | `brew install node`              |

If you're on a Mac, [Homebrew][1] will vastly ease the process of installing
and managing these dependencies.

[1]: https://brew.sh/

## First-Time Setup

When you unpack this code, or when there are major changes to the software,
it's necessary to download additional libraries and tools that this service
depends on. This is handled via the `npm` package manager, part of the NodeJS
distribution.

Run `npm install` in this directory to install these dependencies.

## Running This Service

To run this service locally for development, run `npm start`. The server will
start immediately, and will be available at http://localhost:3000/ by default.

To shut down the service, press Ctrl-C (or on some Windows versions,
Ctrl-Break) to interrupt the program. It will shut down immediately.

To change the port number set the `PORT` environment variable to the desired
port number and start the service.
