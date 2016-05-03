'use strict';

import domReady from 'detect-dom-ready';

domReady(() => {
    require.ensure([], (require) => {
        // use explicit module index as a concession to Mac OS developers. Stops "require('./App')" from resolving to
        // this very file.
        require('./App/index');
    });
});
