# Login Box [![Build Status](https://circleci.com/gh/login-box/login-box.svg)](https://circleci.com/gh/login-box/login-box)

The following is largely a statement of ambition, not of fact.
**This project is not yet ready for use.**
In particular, key features like "logging in" aren't implemented.
If you install it right now, I will not support you except to say 'burn your database and start over' if you want to run a production version later.

-----

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

An easy-to-deploy login server for your applications.

Login Box is designed to handle logins (and single sign-on) for your application.

* It's secure: only authorized apps can authenticate users, and users never have to present their private credentials to individual applications.

* It's easy to use: press the button, register your app, and use the REST API to start authenticating users right away.

* It's well-designed: when managing users itself, Login Box follows industry best practices regarding user security, from password hashes to 2FA. And it's all configurable.

## Running Locally

Got a copy of the code? Want to tinker with it? Great.

1. Build the code:

        ./gradlew stage

2. Create a database:

        createdb login-box
        # Adjust the URL as appropriate.
        export DATABASE_URL=postgres://me@localhost/login-box

3. Run migrations.

        foreman start migrate

3. Start the server. By default, it runs on port 5000, but you can change the port with the `PORT` environment variable:

        foreman start web

4. Visit http://localhost:5000/ to open the app.

To shut down the server, hit Ctrl-C (Ctrl-Break on Windows).
