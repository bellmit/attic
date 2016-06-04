'use strict';

/* -- defaults -- */
var config = module.exports = {
  AUTH0_DOMAIN: 'unreasonent.auth0.com',
  AUTH0_CLIENT_ID: 'F3EFeKAOmYjNsqgYJ3yEo23ejqHXdlJK',
  STATIC_ASSET_PATH: undefined,
};

/* -- load exported config values from env, if set -- */
var keys = require('lodash/keys');
keys(config).forEach(function(key) {
  var env = process.env[key];
  if (env)
    config[key] = env;
});
