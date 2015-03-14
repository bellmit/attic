# Protocol Sketch

The following protocol cribs _heavily_ from JA-SIG's CAS protocol, with some improvements around non-browser clients.

## Authenticating From Scratch (HTTP Cookies)

Service applications are generally on their own for determining which authentication flow best fits their specific needs. However, as a starting point, it's reasonable to assume that requests that accept text/html come from a cookie-aware user agent such as a browser. In these cases, Login-Box can use cookies to automatically present tokens back to the service application after login.

0. (Beforehand) The service application administrator configures the service application with the login-box begin-login and verify URLs, and the secret issued when registering the application. (See below.)

1. The user's client application submits a request an authentication-requiring portion of a service application.

        client -> service: access protected resource
            POST /profile HTTP/1.1
            Accept: text/html

2. The service application makes a request to Login-Box to begin authenticating the user and instruct Login-Box how to return the user to the service application post-authentication.

        service -> login-box: initiate login
            POST /begin-auth HTTP/1.1
            Authorization: Bearer YzJhNTc5YzgtNzJjMS00MTM3LWE5MmMtNGMxZmMzODc5YzIw
            Content-Type: application/x-login-box+json
            Accept: application/x-login-box+json; version=1
            
            {
                "return": {
                    "url": "https://app.example.com/profile",
                    "via": "redirect"
                }
            }

3. Login-Box replies with a token and the login URL to redirect the user to:

        login-box -> service: login initiated
            HTTP/1.1 200 Ok
            Content-Type: application/x-login-box+json
            
            {
                "loginToken": "54b81796-958c-4dcd-ba51-44b7a463064f",
                "valid": {
                    "notBefore": "2015-03-07T00:00:00Z",
                    "notAfter": "2015-03-07T00:05:00Z",
                },
                "loginUrl": "https://login.example.com/login/e0b87aed-ba57-466b-a392-5e9252a672aa"
            }

    Login-Box generates and replies with a login token immediately. However, this token cannot yet be validated: the user must first authenticate themselves to Login-Box.

4. The service application fails the request and redirects the client application to the login URL

        service -> client: login required, see login page at
            HTTP/1.1 303 See Other
            Location: https://login.example.com/login/e0b87aed-ba57-466b-a392-5e9252a672aa
            Set-Cookie: loginToken=54b81796-958c-4dcd-ba51-44b7a463064f; Path=/; Secure; HttpOnly

5. The client application authenticates successfully with Login-Box at the login URL (covered in detail below):

        client -> login-box: login requested
            GET /login/e0b87aed-ba57-466b-a392-5e9252a672aa
            Accept: text/html
        (...login-box and client further negotiate credentials...)

6. Login-Box redirects the client application to the return URL provided by the service application:

        (...in response to client's final credentials submission...)
        login-box -> client: login completed, return to service
            HTTP/1.1 303 See Other
            Set-Cookie: session=8593aac3-acc0-4763-b853-91ac6ebb202a; Path=/login; Secure; HttpOnly
            Set-Cookie: rememberMe=f8fa1b87-0171-4906-ab5a-fbf552d0b6b8; Expires=Sat, 07 Mar 2025 01:00:00 GMT; Path=/login; Secure; HttpOnly
            Location: https://app.example.com/profile

    Login-Box also uses this opportunity to provide its own secondary credentials, as appropriate.

7. The client application requests the return URL from the service application, presenting the service token it obtained as a cookie in step 4 (unfortunately, but unavoidably, the POST intent has been lost):

        client -> service: access protected resource with token in cookie
            GET /profile HTTP/1.1
            Accept: text/html
            Cookie: loginToken=54b81796-958c-4dcd-ba51-44b7a463064f

8. The service application makes a request to login-box to verify the presented service token:

        service -> login-box: verify login token
            POST /verify HTTP/1.1
            Authorization: Bearer YzJhNTc5YzgtNzJjMS00MTM3LWE5MmMtNGMxZmMzODc5YzIw
            Content-Type: application/x-login-box+json
            Accept: application/x-login-box+json; version=1
            
            {
                "loginToken": "54b81796-958c-4dcd-ba51-44b7a463064f"
            }

9. Login-Box returns a successful verification message for the service token:

        login-box -> service: login token okay
            HTTP/1.1 200 Ok
            Content-Type: application/x-login-box+json
            
            {
                "serviceToken": "d1aa1bdf-9852-433e-ba1d-0fbf4bdc197b",
                "username": "example-user",
                "userId": "97ffcc6e-871a-4cda-bf63-70ab401c18c7",
                "valid": {
                    "notBefore": "2015-03-07T00:00:10Z",
                    "notAfter": "2015-03-07T00:05:10Z",
                    "renew": "reverify"
                },
            }

    Login-Box also indicates the validity policy for the returned service token: it will be valid between midnight and five after midnight on March 7th 2015, UTC, and can be renewed by re-verifying it before it expires. (See step 8 for details on verification.)

10. The service application grants access to the resource:

        service -> client: access granted
            HTTP/1.1 200 Ok
            Content-Type: text/html
            Set-Cookie: serviceToken=d1aa1bdf-9852-433e-ba1d-0fbf4bdc197b; Path=/; Secure; HttpOnly
            Set-Cookie: loginToken=; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Path=/; Secure; HttpOnly
            
            <!DOCTYPE html><!-- ... -->

    Even though the service application is aware of the service token's expiry date, it is not presented in the cookie. Token cookies are treated as session cookies, to discourage them from being leaked via browsers' disk storage for cookies.

    At this point, the service application can continue revalidating the service token on each request, or sporadically; the user never has to return to the login service. If the service application revalidates the service token, it must submit the following request:

        service -> login-box: verify service token
            POST /verify HTTP/1.1
            Authorization: Bearer YzJhNTc5YzgtNzJjMS00MTM3LWE5MmMtNGMxZmMzODc5YzIw
            Content-Type: application/x-login-box+json
            Accept: application/x-login-box+json; version=1
            
            {
                "serviceToken": "d1aa1bdf-9852-433e-ba1d-0fbf4bdc197b"
            }

    Login-Box will renew the token if appropriate, and respond with a verification message:

        login-box -> service: service token okay
            HTTP/1.1 200 Ok
            Content-Type: application/x-login-box+json
            
            {
                "username": "example-user",
                "userId": "97ffcc6e-871a-4cda-bf63-70ab401c18c7",
                "valid": {
                    "notBefore": "2015-03-07T00:00:10Z",
                    "notAfter": "2015-03-07T00:32:00Z",
                    "renew": "reverify"
                },
            }

### Exceptional Cases

* The login token is abandoned:

    If the user never completes authentication, or the login token's notAfter expiry time passes, the login token becomes invalid silently.

* The login token expires:

    Login tokens are automatically renewed whenever the user presents a credential, which normally keeps them valid for the duration of the login attempt. However, exceptional network weather can cause a single login step to take more than the allotted time. In these cases, the behaviour depends on where the user is in the process:

    * The user next hits Login-Box: the login token is discarded and the user is redirected back to the service application at the return URL. Then see the following point.
    
    * The user next hits the service application: When the service application attempts to validate the login token, it instead receives a failure response:

            login-box -> service: login token expired
                HTTP/1.1 400 Bad Request
                Content-Type: application/x-login-box+json
                
                {
                    "reasons": {
                        "loginToken": "expired"
                    }
                }

        The service application can determine at this point whether to retry login (most likely) or whether to present an error to the user advising them of the problem. Either way, the service application must destroy the login token; Login-Box will no longer honour it.

* The login token has been previously validated:

    Normally, service applications convert login tokens to service tokens at most once. However, bugs and user behaviour can cause multiple attempts to validate the same login token.

    When Login-Box validates a login token, it renews the login token one final time for a much shorter period (tens of seconds, rather than minutes). During this final renewal, attempts to validate the login token will produce the same result as the original attempt to validate it (a service token, or an error) would have, but will not further renew the login token.

    After this final renewal elapses, the login token has expired and will be handled as above.

* The service token expires:

    Service tokens must also be renewed periodically. This can be done on every request, but more more efficient schemes are possible. When attempting to validate a token, the service application may discover that the token has expired:

        login-box -> service: service token expired
            HTTP/1.1 400 Bad Request
            Content-Type: application/x-login-box+json
            
            {
                "reasons": {
                    "serviceToken": "expired"
                }
            }

    Service applications must destroy the client application's token immediately, and cease honouring it.

## Authenticating From Scratch (OAuth2 Bearer Tokens)

TBD, but broadly follows the same flow as cookie-based auth, within the confines of OAuth 2.

## Logging Out

Logging out of a single service application is easy: the application can directly destroy the service token, without coordinating with Login-Box or with other applications. However, this is often inappropriate; in particular, as long as the user's Login-Box session and remember-me credentials remain valid, the user will be able to log back into a service application without further re-authentication, which is not what most people expect after logging out. Login-Box provides a centralized logout protocol:

1. The client application submits a logout request _directly to Login-Box_:

        client -> login-box: log me out
            POST /logout HTTP/1.1
            Cookie: session=8593aac3-acc0-4763-b853-91ac6ebb202a
            Cookie: rememberMe=f8fa1b87-0171-4906-ab5a-fbf552d0b6b8

2. Login-Box invalidates _all_ of the user's service tokens, and the session and rememberMe credentials, immediately. Attempts to validate an invalidated service token produce the following validation message:

        login-box -> service: service token expired
            HTTP/1.1 400 Bad Request
            Content-Type: application/x-login-box+json
            
            {
                "reasons": {
                    "serviceToken": "logged-out"
                }
            }

3. For each valid service token issued to the user, Login-Box sends an HTTP notification to the corresponding service application if the application has a "logout" notify URL (see below):

        login-box -> service: user logged out
            POST /notify/logged-out HTTP/1.1
            Content-Type: application/x-login-box+json
            
            {
                "serviceToken": "d1aa1bdf-9852-433e-ba1d-0fbf4bdc197b"
            }

    Login-Box will try each notify URL at most once, and will not honour redirects or do anything with any responses from the service application.

    (Actually, this should happen asynchronously, but I've been trying to avoid _requiring_ worker dynos, and doing async work without worker dynos is awkward at best.)

4. Login-Box will redirect the client application to its own login page:

        login-box -> client:
            HTTP/1.1 303 See Other
            Location: https://login.example.com/

Optionally, the original logout request can include a `app` form parameter. This parameter, if provided, must be a valid service application name (see below); if provided and valid, the final redirect will be to the named service application's registered URL. For example, a service application might embed the following HTML on a page to trigger logout:

    <form action="https://login.example.com/logout" method="post">
        <input type="hidden" name="app" value="example-application">
        <input type="submit" value="Log out">
    </form>

When clicking this button, users will be logged out, then redirected back to the root URL of the `example-application` app. The provided app name does _not_ have to be the name of the originating application: suites of apps can, for example, use a common entry point, and redirect there after logging out.

`GET` requests will not trigger the logout process, regardless of parameters.
