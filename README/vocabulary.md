# Login-Box Vocabulary

* User: a human.

* Application: a program.

    * Service application: a program requiring authentication. Generally a web server, since Login-Box is focussed on HTTP authentication.

        * Service library: a reusable software library for validating tokens against Login-Box from within a service application.

        * Service applications must be registered with Login-Box.

        * Login-Box's administrative and user-facing interfaces are "internal" service applications, and need not be explicitly registered. (They also can't be unregistered.)

    * Client application: a program operated by the user, to access a service application. May be a browser, may be a non-browser HTTP user agent such as a shell script or mobile client.

        * Client library: a reusable software library for communicating with Login-Box from a client application, and for integrating Login-Box tokens into requests to service applications.

        * Client applications need not be registered with Login-Box.

* Credential: evidence, held by the user, that can establish a user's identity.

    * Users _present_ credentials to Login-Box, which verifies them to _authenticate_ the user.

    * Login-Box may demand the presentation of multiple credentials during authentication.

    * Users are _not_ expected to present credentials to service applications.

    * Primary credential: a long-lived credential managed by the user or the user's administrators.

    * Session credential: a machine-readable credential issued by Login-Box as a substitute for primary credentials. Session credentials are valid for a limited amount of time, but can be renewed.

    * Remember-Me credential: a machine-readable credential issued by Login-Box as a substitute for primary credentials. Remember-Me credentials are valid for a much larger period of time, but may be used at most once.

* Token: evidence, held by client applications, that can establish that they operate on behalf of a user.

    * Login-Box _issues_ tokens to client applications after authenticating a user. Client applications _present_ tokens to service applications to _identify_ the user. Service applications _verify_ tokens with Login-Box to determine the identity of the user.

    * Users are generally not expected to manage tokens themselves, but there may be exceptions.

    * Users can revoke tokens at any time by using Login-Box.

* Directory: a database containing sets of credentials for a group of users. Login-Box supports multiple directories.

    * Read-only Directory: a database that Login-Box can use for authentication, but cannot manage itself.

* User ID: a machine-readable, stable identifier for a user. If two authentication attempts produce the same user ID, they authenticate the same user. (The inverse need not be true: a user may be issued multiple user IDs, under rare circumstances.)

* Username: a human-readable credential known to the user, to login-box, and to service applications.

    * Usernames are not used as user IDs, as some applications permit users to change their usernames, or to hold multiple usernames.

* Email address: a human-readable credential that can also be used to deliver email to the user.

    * Verified email address: an email address that has been verified as to its ability to receive mail for the designated user. Email addresses are unverified by default, and can be verified by sending them mail.

* Password: a credential known to the user only, which can be typed.

    * For some directories, Login-Box includes its own tool to allow users to _change_ and to _reset_ their password.
