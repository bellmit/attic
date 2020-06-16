/*
 * This program provides a WebSocket-based example of a chat service. Clients -
 * primarily web browsers - connect to the chat service on behalf of users, and
 * allow those users to send messages to one another. This chat service is
 * deliberately extremely simplified, to make the code easier to understand.
 * Features such as user identities, multiple distinct channels, private
 * messages, or message history, which are the standard for services such as
 * IRC, Slack, Discord, SnapChat, Facebook Messenger, and so on are not
 * implemented.
 *
 * In fact, this service is so simplified that it doesn't even verify that
 * messages are valid in any way. If you send this service garbage, it will
 * send garbage to all connected users on your behalf. A more serious
 * implementation would do some basic message validation before processing
 * and broadcasting a message.
 *
 * It also provides a web server that delivers an HTML-based chat client for use
 * with this service.
 */

/*
 * This service builds on the Express web frameowrk. Express is the de facto
 * standard web framework for NodeJS, and there are a wide range of extensions
 * and libraries that customize Express for specific use cases. It's also much
 * easier to work with than Node's built-in web support.
 *
 * See also:
 *  - Express: Hello world example
 *    https://expressjs.com/en/starter/hello-world.html
 *  - NodeJS: Modules
 *    https://nodejs.org/api/modules.html
 *  - NodeJS: require()
 *    https://nodejs.org/api/modules.html#modules_require_id
 */
const express = require('express');

/*
 * A chat service is asynchronous - meaning that messages can flow in either
 * direction at any time. HTTP itself is rigidly synchronous, with a strict
 * one-request-one-response order to it, but WebSockets provide a portable,
 * standardized layer over HTTP that provides asynchronous messaging.
 *
 * The express-ws library provides WebSocket support for the Express web
 * framework. This must be configured immediately after the 'app' object is
 * created, so that express-ws can make changes to the app's routing logic
 * before any routes are set up.
 *
 * See also:
 *  - express-ws: README
 *    https://github.com/HenningM/express-ws#express-ws-
 */
const installWs = require('express-ws');

/*
 * The convention recommended by Express is that an application should be
 * represented by a single, top-level 'app' value, holding the Express
 * configuration.
 *
 * See also:
 *  - Express: express()
 *    https://expressjs.com/en/4x/api.html#express
 */
const app = express();
installWs(app);

/*
 * The 'express.static' middleware provides some services Express can use to
 * serve files from a directory - in this case, the 'public' subdirectory of
 * this project.
 *
 * The 'app.use' function attaches middleware to our Express application, so
 * that when the application is running, it can serve static files. In this
 * case, we mount it over the entire app: any web request that GETs a path that
 * exists in the 'public' directory will be handled by the middleware. The
 * middleware also serves the 'index.html' file in a directory (if it exists)
 * whenever a client requests the directory itself.
 *
 * The 'public' directory for this project, in turn, contains all the HTML,
 * Javascript, and CSS files needed to run a simple chat client connected to
 * this server. Accessing this server's root URL will serve 'public/index.html',
 * which contains our chat client. This gives users an easy way to connect to
 * the server and interact with other users.
 *
 * See also:
 *  - Express: Serving static files in Express
 *    https://expressjs.com/en/starter/static-files.html
 *  - Express: express.static()
 *    https://expressjs.com/en/4x/api.html#express.static
 *  - Express: Using middleware
 *    https://expressjs.com/en/guide/using-middleware.html
 *  - Express: app.use()
 *    https://expressjs.com/en/4x/api.html#app.use
 */
app.use(express.static('public'));

/*
 * The role of this chat server is to receive messages as they arrive from each
 * chat client and deliver them to all connected chat clients. To do this, we'll
 * need to keep track of connected clients. An array of connections provides
 * enough state to implement this chat system.
 *
 * As this service operates, the 'clients' array will grow and shrink. At any
 * given time, it will contain one WebSocket connection per client, which this
 * service uses to receive messages from clients, and to deliver messages back
 * to clients.
 *
 * This array must be subject to an invariant: no connection can appear in the
 * array more than once at a time. Much of the following code will behave
 * incorrectly if a connection appears in the 'clients' array in two places.
 *
 * See also:
 *  - MDN: Array
 *    https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array
 */
const clients = [];

/*
 * In order to begin delivering messages to a connection, something needs to add
 * the connection to the list of clients. This function does so, delegating to
 * the Array type's built-in 'push' method to add clients at the end. (There's
 * no specific need to insert values at any position, and "at the end" is conventional.)
 *
 * To maintain the invariant that no connection is present in 'clients' more
 * than once at a time, this function must not be called with a connection
 * already in 'clients'. In other words, it's up to the programmer to use it
 * correctly. In a larger system, this function might instead _verify_ the
 * invariant before modifying 'clients', but in a simple system that can fit in
 * one programmer's head, that level of validation is unnecessary.
 *
 * See also:
 *  - MDN: Array.prototype.push()
 *    https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/push
 */
function addClient(connection) {
  clients.push(connection);
}

/*
 * When a client disconnects, we need to remove it from the array of clients.
 * The websocket connection library we'll be using is capable of handling
 * messages sent on disconnected sockets, and leaving disconnected clients in
 * the list wouldn't the server to crash, but if we don't remove the clients
 * from the array then the array will continue to grow every time clients
 * connect, eventually exhausing memory.
 *
 * This function has an additional precondition: it must only be passed
 * connections that are in the 'clients' array. Again, in a larger system, this
 * function might be written to verify that precondition rather than taking it
 * for granted, but this program is simple enough that double-checking isn't
 * necessary.
 */
function removeClient(connection) {
  /*
   * Javascript's built-in array type doesn't have a "remove value" function,
   * so this is a little more involved than adding a client. First, we use the
   * built-in 'indexOf' function to find the position of the connection in the
   * 'clients' array (which will be -1 if the connection isn't in 'clients'
   * anywhere).
   *
   * Then, using that index, we use 'splice' to remove one element at that
   * index. That element will necessarily be 'connection' itself, provided
   * that 'connection' is actually present in the array.
   *
   * The precondition that 'connection' must be in 'clients' is a consequence
   * of this implementation. If 'connection' is not in 'clients', 'indexOf'
   * will return -1. The array 'splice' method interprets a negative index as
   * an offset from the end of the array, and the index -1 always represents
   * the final element of the array. Calling this function with a connection
   * not present in 'clients' will therefore remove the most recently-added
   * connection, instead.
   *
   * See also:
   *  - MDN: Array.prototype.indexOf()
   *    https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/indexOf
   *  - MDN: Array.prototype.splice()
   *    https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/splice
   */
  const connectionIndex = clients.indexOf(connection);
  clients.splice(connectionIndex, 1);
}

/*
 * When this server receives a message, it delivers it to every connected
 * client. This function implements that: it takes a message (any value the
 * underlying websocket library can send) and sends it to every connection in 'clients'.
 *
 * See also:
 *  - MDN: WebSocket.send()
 *    https://developer.mozilla.org/en-US/docs/Web/API/WebSocket/send
 *  - MDN: Array.prototype.forEach()
 *    https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/forEach
 */
function broadcast(message) {
  clients.forEach((connection) => connection.send(message));
}

/*
 * This route provides the chat service we've defined as a WebSocket endpoint.
 * It's mounted at the root of the application since it's the primary purpose of
 * the service itself.
 *
 * See also:
 *  - Express: Routing
 *    https://expressjs.com/en/guide/routing.html
 *  - websockets/ws: Class: WebSocket
 *    https://github.com/websockets/ws/blob/master/doc/ws.md#class-websocket
 */
app.ws('/', (connection) => {
  /*
  * The translation from WebSocket operations to chat operations is
  * straightforward.
  *
  * First: when a WebSocket connection is created, register it as a client. The
  * express-ws library implicitly guarantees that this will be called exactly
  * once per client, which means that we easily satisfy the invariant we
  * established for the 'addClient' function above.
  */
  addClient(connection);

  /*
   * Second: when a WebSocket connection closes, remove it from our array of
   * clients. The express-ws library provides two implicit guarantees that we
   * count on: first, that this will only ever happen once per connection, and
   * second, that it will only happen if the connection has successfully been
   * opened. Between these to properties, this easily satisfies the invariant
   * that we can only remove clients that are in 'clients', with respect to
   * the connection we receive and register above.
   */
  connection.on('close', () => {
    removeClient(connection);
  });

  /*
   * Third: when a WebSocket connection delivers a message from a client,
   * broadcast it. This is the real meat of the chat service.
   */
  connection.on('message', (message) => {
    broadcast(message);
  });
});

/*
 * Finally, start the server listening on a port and serving pages & requests.
 *
 * The "PORT" environment variable is a convention used by many cloud platforms,
 * including Heroku, to tell applications what TCP port to listen on for web
 * requests. This server supports the PORT environment variable so that it can
 * be easily deployed to the cloud.
 *
 * However, this environment variable isn't set when you run this server
 * locally. When the environment variable is unset, fall back to port 3000 (a
 * convention used by the Express documentation).
 *
 * The 'app.listen' function sets up a listening network socket, along with all
 * of the event handlers Express needs to handle the application described
 * above. Once it returns, the application is running and can handle HTTP
 * requests on 'port'. The program will stay running as long as the socket is
 * open, and since nothing in this program closes the socket, it will try to run
 * forever. In practice, the program will shut down when the user stops it with
 * Ctrl-C or with the 'kill' command.
 *
 * See also:
 *  - Heroku: Runtime Principles
 *    https://devcenter.heroku.com/articles/runtime-principles
 *  - NodeJS: process.env
 *    https://nodejs.org/api/process.html#process_process_env
 *  - Express: app.listen()
 *    https://expressjs.com/en/4x/api.html#app.listen
 */
const port = process.env.PORT || '3000';
app.listen(port);
