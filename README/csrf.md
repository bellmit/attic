# Cross-Site Request Forgery Prevention

Login Box attempts to prevent [CSRF](https://www.owasp.org/index.php/CSRF)
attacks _against Login Box itself_. It does not attempt to prevent them against
the protected applications.

## Threat Model

* A _user_ uses a _browser_ to interact with _Login Box_, _protected sites_,
    and _foreign sites_.

* A _foreign site_ may include code that causes the browser to make requests on
    the user's behalf, for example the following:

        var form = document.createElement('form');
        form.setAttribute('method', 'post');
        form.setAttribute('action', 'http://victim-login-box-server.example.com/login');

        var username = document.createElement('input');
        username.setAttribute('name', 'username');
        username.setAttribute('value', 'victim-username');
        form.appendChild(username);

        var password = document.createElement('input');
        password.setAttribute('name', 'password');
        password.setAttribute('value', generatePasswordAttempt());
        form.appendChild(password);

        form.submit();

* When the user visits the foreign site, their browser downloads and executes the code, causing the browser to make requests to Login Box.

If this attack appears on a popular site, many passwords may be attempted very
quickly using the userbase as unwitting relays. Other interesting attacks may
use the same basic technique.

This same technique can be used to inject requests using browsers already
signed into Login Box, which will be applied with the user's credentials.

## Mitigation

Login Box's `CsrfBundle` provides tools to mitigate this attack. Any request
can _issue_ a CSRF token, which is a one-shot secret value delivered to the
browser in an HTTP response body. CSRF tokens are paired with CSRF sessions,
which are long-lived secret values delivered to the browser in a `Set-Cookie`
header:

    c: GET /login HTTP/1.1
    c:
    s: HTTP/1.1 200 Okay
    s: Set-Cookie: csrfSecret=SESSION-SECRET;HttpOnly;Secure
    s: Content-Type: application/json
    s:
    s: { "csrfToken": "TOKEN-SECRET" }

To perform a request, the browser must present both secrets to Login Box:

    c: POST /login HTTP/1.1
    c: Cookie: csrfToken=SESSION-SECRET
    c:
    c: { "username": "beryl", "password": "my-password", "csrfToken": "TOKEN-SECRET" }

The server will match up the presented `csrfToken` field to a previously-issued
token for the presented `csrfSecret`; if a match is found, the request may
proceed. Otherwise, the server aborts the request with a 400 Bad Request
response. Login Box includes messaging in the HTML version of the response to
inform the user as to the problem, if the HTML is presented to the user.
(Because of the variety of CSRF techniques in the wild, this cannot be
guaranteed.)

Tokens may be used at most once; a second attempt to use the same token will
treat the token as invalid.

Foreign sites cannot easily obtain a token value that is valid _for the user
visitng their site_: the browser will not present the `csrfSecret` to a foreign
site, preventing foreign sites from making their own requests using the user's
credentials to obtain a valid token.

Since CSRF tokens are one-shot in Login Box, they're also resistant to replay:
a foreign site that obtains a valid token can make at most one request using
it, and may make no requests if the user consumed the token first.

For API documentation, see the `CsrfBundle`, `CsrfValidator`, and `CsrfToken`
classes. For secret generation, see the `SecretGenerator` class.

## Tradeoffs

CSRF tokens are also embedded in HTML forms. Since they're valid once,
duplicate submission and the browser Back button both behave poorly in the
presence of a CSRF token.

Since Login Box is security infrastructure, this tradeoff is acceptable for
stronger forgery prevention measures.

## Security Concerns

Third-party code present in Login Box pages can scrape the page to discover
valid tokens as they're issued. Login Box must avoid the use of third-party
page code as far as possible, and must ensure that code delivered to the page
was delivered from Login Box itself.

The `csrfSecret` cookie persists for the duration of a browser session, and
Login Box will reuse it if present rather than issuing a new secret. There is
no provision for forcibly issuing a new `csrfSecret` cookie from within Login
Box. So long as the `csrfSecret` value is used _solely_ for CSRF prevention,
and not as a session authenticator, this should be safe.

There must be no mechanism available to force the value of the `csrfSecret`
cookie.

Weak secret generation can compromise the security of CSRF tokens. Login Box
uses Java's UUID class to generate secrets, which in turn uses SecureRandom
under the hood to access `/dev/urandom` or platform equivalents.

## Rejected Approaches

* Jersey [CsrfProtectionFilter](https://jersey.java.net/nonav/apidocs/latest/jersey/org/glassfish/jersey/client/filter/CsrfProtectionFilter.html):
    reliance on a custom `X-Requested-By` header forces reliance on Javascript
    on the client side. The approach used in Login Box relies only on built-in
    browser tools.
