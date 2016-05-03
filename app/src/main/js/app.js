'use strict';

import domReady from 'detect-dom-ready';

domReady(() => {
    require.ensure([], (require) => {
        console.log("started");
    });
});
