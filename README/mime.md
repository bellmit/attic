# MIME Types

Login Box supports two MIME types each for requests and for responses.
Negotiation follows the HTTP/1.1 RFC, using the `Accept` header to decide which
supported content type to return and the `Content-Type` header to decide which
content type to parse in a request.

The supported types are:

* `application/x-login-box+json; version=1` - supported for both requests and
    responses. This is a JSON-based type whose notional schema varies by
    message. See the documentation for each endpoint for a description of the
    schemata.

* `text/html` - supported for responses only. This is HTML 5, with support for
    browser-based rendering and interaction.

* `application/x-www-form-urlencoded` - supported for requests only. This is
    forms as described in HTML 5. Field names vary per message; see the
    documentation for each endpoint.
