# Webpack Starter Pack

To run the app locally, run:

    npm run build
    npm run webpack       # equivalent to build, or use…
    npm run webpack-watch # run webpack continuously while editing
    npm run web           # start HTTP server locally on ${PORT:-4000}

To run tests:

    npm run test
    npm run karma         # equivalent to test, or use…
    npm run karma-watch   # run karma continuously while editing

Source layout:

* `dist`: built app (HTTP content root)
* `src`: app sources (all languages)
    * `src/index.html`: the app's root page, minus script tags.
    * `src/app.jsx`: the app's entry point script.
    * `src/app.less`: the app's entry point style sheet, using Less.
    * `src/components.jsx`: the app's React components.
    * `src/components.less`: the components' style sheets, using Less.
* `test`: app unit tests (Karma + Chai)
    * `test/bundle.js`: the test suite's entry point.
    * `test/**/*.spec.js`: individual test spec suites.
    * `test/**/*.spec.jsx`: individual test spec suites.
* `web`: a trivial HTTP server for local development or deployment to Heroku.

Webpack has been configured to read the following extensions:

* `.jsx`: ES2015 Javascript, with JSX syntax for React.
* `.js`: ES2015 Javascript.
* `.less`: Less stylesheets.

Switch to Sass if you like.

Much of the rationale for these choices is laid out [here](http://grimoire.ca/dev/webpack).
