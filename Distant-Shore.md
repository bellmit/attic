# Distant Shore

A web-based, social tactical RPG.

## Up next

* match making and match wrapup ([[pull request|https://github.com/unreasonent/distant-shore-html5-client/pull/24]], [[review app|http://ue-ds-html5-client-stagi-pr-24.herokuapp.com/]])

    > LoneFoxLaughing: i.e. entering and exiting the active gameplay loop?
    >
    > ojacobson: Yeah, with a dummy loop in the middle three buttons: player A wins, player B wins, match abandoned)

## Technology

* An HTML5 client (React, Redux, ES6, the works) delivered by a mostly-static asset server.
    * This is realized in the [[html5-client|https://github.com/unreasonent/distant-shore-html5-client]] repo, and deployed in the ue-ds-html5-client family of Heroku apps.
* An API server (JAX-RS, Dropwizard) providing JSON-over-HTTP hypermedia-ish endpoints.
    * This is realized in the [[api|https://github.com/unreasonent/distant-shore-api]] repo, and deployed in the ue-ds-api family of Heroku apps.
    * Supports whitelisted CORS requests from browsers.
* Users authenticated by Auth0, providing JWT tokens for use in API calls.

## Deployment

Production:

* Client at http://distant-shore.com/
* API at http://api.distant-shore.com/

Staging (mostly idle):

* Client at http://staging.distant-shore.com/
* API at http://api.staging.distant-shore.com/

Staging API is also the default API for local development instances of Distant Shore.

Review apps:

* Client review apps are listed in Github pull request comments. These are backed by the staging API.
* API review apps are listed in Github pull request comments. There is presently no good way to use review APIs locally.