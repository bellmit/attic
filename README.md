# Distant Shore HTML5 Client

This is the UI you get at https://distant-shore.com/.

To run the client locally, run:

    npm run build
    npm run web

Alternately, you can run the build steps separately:

    npm run lessc   # no watch mode for lessc, sadly
    npm run webpack # or webpack-watch, for live updates while editing

You will need the Distant Shore services running locally on their default ports.

To run the unit tests for the UI, _you do not need the Distant Shore services_:

    npm run test
    npm run karma       # equivalent to test
    npm run karma-watch # autostarts Firefox; manually connect other browsers

## Configuration

All of the config keys default to values appropriate for staging. You may use these values for local development. To change them, export the named environment variable when starting the web server.

* `API_URL`: the base URL of the Distant Shore API. Must not end in a slash; the app isn't that smart about URL construction.

* `AUTH0_CLIENT_ID`: a public client ID for the matching Auth0 application. This must match the client secret used in the API server; Auth0 will issue tokens signed by that secret when the user logs in with this client.

* `AUTH0_DOMAIN`: the Auth0 authentication domain for this app. Must also match the client secret used in the API server.
