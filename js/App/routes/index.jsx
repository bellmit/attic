'use strict';

module.exports = {
    path: '/',

    getComponents(nextState, callback) {
        require.ensure([], (require) => {
            callback(null, require('./components/App'));
        });
    },

    getIndexRoute(location, callback) {
        require.ensure([], function (require) {
            callback(null, {
                component: require('./components/Landing'),
            })
        })
    },

    getChildRoutes(location, callback) {
        require.ensure([], function (require) {
            callback(null, [
                require('./routes/Squad'),
            ])
        })
    },
};
