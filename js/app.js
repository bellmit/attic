'use strict';

import domReady from 'detect-dom-ready';
import asset from './app/asset';

domReady(() => {
  // Load script chunks from Aerobatic's CDN, if possible. This must be done
  // before the first async load; we might as well do it here, at the entry
  // point. However, we have to wait for the DOM to be ready to ensure that
  // Aerobatic's injected <script> block has evaluated.
  __webpack_public_path__ = asset(__webpack_public_path__);

  // use explicit module index as a concession to Mac OS developers. Stops
  // "require('./app')" from resolving to this very file.
  require('./app/index');
});
