# Presenting Credentials

`GET` requests for a login URL present a UI depending on the state of the login token:

1. A brand-new login token will generate a page requesting all mandatory credentials: normally, this is username or email address, password, and an option for enabling remember-me authentication.

2. If the user has already successfully presented all mandatory credentials, the page requests any additional credentials configured for that user, such as TOTP tokens.

3. If the user has successfully present both mandatory and additional credentials, the user is redirected back to the originating service application.

In all cases, Login-Box presents (and requires) a CSRF-preventing nonce unique to each form, to discourage mechanization of the login process.

Login-Box does not issue session or remember-me credentials until the third step.

If the user presents a valid session credential or a valid remember-me credential at the first step, Login-Box will use that credential to identify the user instead of requesting a username and password. Login-Box will still request additional credentials, if configured, if the user only presents remember-me credentials without a valid session credential.

# Changing Credentials

All credential changes for the internal directory must be done by the user themselves, via Login-Box's web UI. No exceptions (and no administrator resets). As with logging in, all of the following steps are guarded by CSRF tokens.

The web UI requires that the user present mandatory credentials (even if they have already presented them), and any configured additional credentials, before changing their credentials.

Changed credentials in Login-Box's internal directory are stored as follows:

* Usernames: stored in the application data store, in plain text.

* Email addresses: stored in the application data store, in plain text. This information is no more sensitive than username, and can be presented as a username credential.

* Passwords: bcrypt with a configurable cost value, defaulting to 10.

* TOTP secrets: stored in the application data store, encrypted using Fernet. (? Probably. TBD, because of API availability vs server language. Something _not_ home-brew, at any rate.) The key will be derived from a randomly-generated value at deployment time, stored in Heroku's config keys. See https://devcenter.heroku.com/articles/app-json-schema#env for details of secret generation.

The guiding principle is that obtaining a copy of the database should let you authenticate users, but should _not_ let you use users' credentials elsewhere, even when (not if, sadly) users reuse credentials.

# Password Reset

The built-in directory supports email password resets. The protocol is as follows:

1. The user selects "Forgot password" on a login form, and _enters_ their email address.

2. Login-Box informs the user that instructions for resetting the password have been emailed to their address. (This occurs regardless of whether the email address is known to Login-Box.)

3. If the email address is known to login-box, _and is confirmed_, Login-Box generates a random nonce and emails it to the address, along with instructions on how to use that nonce to reset their password. The nonce is tagged with the IP address it was requested by, and the date and time at which it was issued. Password reset nonces expire after 1 hour if not used.

4. If the user receives the message and presents the nonce, they may enter a new password for their user ID. They are then redirected back to the normal login flow (on a newly-generated login token).

Password resets do not check additional credentials such as TOTP tokens. However, additional credentials will be checked during the next attempt to log in.

# Email Validation

Newly-created email address credentials in Login-Box's internal directory are not considered "confirmed" initially. Unconfirmed email address credentials may be used to authenticate users, but will _not_ be used to contact users for password resets.

When Login-Box creates a user or changes a user's email address, Login-Box also issues a confirmation email with a random nonce to the user. The nonce is contained in a URL; clicking the URL returns the user to Login-Box, where the nonce can be validated to mark the email address as "confirmed."

When users visit Login-Box's credentials management page and have not confirmed their email address, Login-Box displays a warning and offers to re-send the confirmation email. (This will actually issue a new nonce, rather than re-sending an existing one.)
