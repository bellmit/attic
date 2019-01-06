# Distant Shore

A web-based, social tactical RPG.

## Up next

* match making and match wrapup ([[pull request|https://github.com/unreasonent/distant-shore-html5-client/pull/24]], [[review app|http://ue-ds-html5-client-stagi-pr-24.herokuapp.com/]])

    > LoneFoxLaughing: i.e. entering and exiting the active gameplay loop?
    >
    > ojacobson: Yeah, with a dummy loop in the middle three buttons: player A wins, player B wins, match abandoned)

## Revenue Model

To manage payment costs, we indirect player payments through "challenge tokens" (name not final). Players purchase challenge tokens in blocks of (sizes here) using USD or CAD, then expend those tokens to begin games. We'll follow the now-traditional pricing model of selling small blocks at the highest marginal rate, with increasing discounts for larger blocks of challenge tokens.

To encourage engagement, we also issue players a small stack of free challenge tokens once per day. This allows casual players to build our reach for us by challenging other players, and by keeping the ranked ladder populated.

* Challenge a Friend: 1 challenge token for the challenger, no cost for the responding player.
* Ranked Match: 1 challenge token for each player.
* Practice Match: No challenge tokens.

## Sucks List

Not quite bugs, but definitely a lot like them:

* TLS everywhere. Right now there are lots of issues with mixed content, since `*.herokuapp.com` domains default to TLS and `*.distant-shore.com` domains default to not-TLS. Cross-origin issues exacerbate this (watch the browser console if you accidentally hit the `https://` version of a review app, for example via a Github link).
* Error handling on API requests leaves the client stranded. For example, log out, then visit https://distant-shore.com/squad - browser console spews errors that the user can't see, and the UI never either reports them or recovers. (In fact, it _should_ prompt the user to log in, but there's a more general problem here.)

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