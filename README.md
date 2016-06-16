# Heroku Buildpack for Node Client Apps

This is a tiny buildpack that can be inserted in front of the [Heroku NodeJS
buildpack](https://github.com/heroku/heroku-buildpack-nodejs) to change its
defaults to something more appropriate to client-side apps, where Node is more
of a build chain.

In particular, this sets the default `NPM_CONFIG_PRODUCTION` to `false` instead
of `true`, so that the Node buildpack will install `devDependencies`.

## Usage

```
heroku apps:create
heroku buildpacks:add https://github.com/unreasonent/heroku-buildpack-node-client-shim
heroku buildpacks:add heroku/nodejs
…git push…
```
