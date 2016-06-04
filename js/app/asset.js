'use strict';

/*
 * Inject appConfig into a callback, if available. When appConfig is available,
 * this will invoke callback once, with appConfig as an argument, and return the
 * result of the callback. When appConfig is not available, this will return
 * undefined without invoking the callback.
 */
function appConfig(callback) {
  if (typeof window === 'undefined')
    // Node.
    return undefined;
  if (typeof window.appConfig === 'undefined')
    // Local server.
    return undefined;
  // Aerobatic, or at least a close approximation.
  return callback(window.appConfig);
}

/*
 * Given an asset path (which must be absolute), return an asset URL. This will
 * take into account window.appConfig, if set, and will return CDN URLs on
 * Aerobatic's provided CDN. In environmens where window.appConfig is not set,
 * this will return path, unaltered.
 */
module.exports = function asset(path) {
  return appConfig(config => `${config.STATIC_ASSET_PATH || ''}${path}`) || path;
}
