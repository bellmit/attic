# Goals

* Authentication that works, out of the box.

* A clear protocol that can be understood by normal programmers, not just security experts.

* Users never have to present their primary credentials to services, only to Login-Box.

* Services only need to send users to Login-Box once per session.

* Services are not forced to communicate with Login-Box every request.

* Supports multiple directories, with administrator-defined search order:

    * An internal directory, stored within Login-Box (handles passwords and TOTP, uses bcrypt for passwords)

    * Zero or more SQL user directories supporting acceptable password digests

    * Zero or more external user directories, via a simple credential-fetching API.

* Supports browser clients: once authenticated, per-app tokens are attached to browser sessions via standard cookies, which will automatically be presented back to the app. The complete protocol, and the assumptions underlying the use of cookies, are documented.

* Supports headless clients (probably via OAuth2, or something like it): users can issue a credential to a client app, which can then operate on their behalf without requiring interactive authentication. The issuing user can rescind these tokens at any time.

* Supports non-browser clients: there is a clear protocol for client applications to obtain authentication tokens, and to present these tokens to service applications. The complete protocol, and the assumptions underlying the use of tokens are documented.

* Login-Box can be redecorated to match most sites without requiring repackaging.

* Login-Box _must not_ assume that Login-Box and service applications share a top-level domain. (No cheating with cookie auth.)

# Anti-goals

* Login-box is not meant to be a replacement for Kerberos, AD, OpenDirectory, or other tightly-integrated network auth service.

* In particular, Login-Box is not designed to deal with proxy authentication ($USER presents credential to A, A presents derived credential to B to indicate that $USER is present).
