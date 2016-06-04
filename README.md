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
