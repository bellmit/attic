# Distant Shore HTML5 Client

This is the UI you get at https://distant-shore.com/.

Due to limitations on the Aerobatic tier used to deploy this app, production
deploys happen from the `production` branch, rather than on every merge to
`master`. Please coalesce groups of changes before deploying.

To run the client locally, run:

    npm run build
    npm run local

Alternately, you can run the build steps separately:

    npm run lessc   # no watch mode for lessc, sadly
    npm run webpack # or webpack-watch, for live updates while editing

You will need the Distant Shore services running locally on their default ports.
