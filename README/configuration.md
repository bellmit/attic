# Runtime Configuration

* `PORT`: The HTTP port number to listen on. This will default to 5000 if not set.

* `CORS_ORIGINS`: A comma-separated list of allowed origin patterns (for example, `https://distant-shore.com` for production). If not set, this will default to `http://localhost:*`, which is only appropriate for development.

* `AUTH0_CLIENT_SECRET`: The Auth0 secret used to sign JWT tokens for the API. Authenticated requests will be checked against this secret. **This has no default.** To obtain a development key, use the "Distant Shore Dev" app in Auth0.
