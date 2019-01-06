# How do I run the server?

## Pre-requisites

First, [build](building.md) the server.

You will need the Distant Shore Dev client secret, exported as `AUTH0_CLIENT_SECRET`.

## Locally

Using your shell environment:

```bash
bin/run server
```

Or, using a `.env` file in the root of the project:

```bash
heroku local
```

For details, read the `bin/run` script. It's not very long.
