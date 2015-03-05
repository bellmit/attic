# Login Box

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

An easy-to-deploy login server for your applications.

Login Box is designed to handle logins (and single sign-on) for your application.

* It's secure: only authorized apps can authenticate users, and users never have to present their private credentials to individual applications.

* It's easy to use: press the button, register your app, and use the REST API to start authenticating users right away.

* It's well-designed: when managing users itself, Login Box follows industry best practices regarding user security, from password hashes to 2FA. And it's all configurable.

## Running Locally

Got a copy of the code? Want to tinker with it? Great.

1. Install dependencies. You'll need to re-run this after merging upstream changes, to ensure that the correct versions of various libs and tools are installed:

        npm install

2. Start the server. By default, it runs on port 5000, but you can change the port with the `--port` option:

        node server.js --port 5000

3. Visit http://localhost:5000/ to open the app.

To shut down the server, hit Ctrl-C (Ctrl-Break on Windows).

Alternately, you can start the server with [Foreman](https://github.com/ddollar/foreman):

    foreman start
