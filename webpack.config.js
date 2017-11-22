const path = require('path')
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const UglifyJSPlugin = require('uglifyjs-webpack-plugin')

function resolve(...args) {
    return path.resolve(__dirname, ...args)
}

const environments = {
    production: {
        plugins: [
            // Configure React (and anything else that looks at NODE_ENV) to use
            // "production" mode unless explicitly overridden. This disables most of
            // React's development support, including much of its sanity-checking,
            // but drastically improves the latency of most UI actions.
            new webpack.DefinePlugin({
                'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV || 'production'),
            }),
            // Remove unused code from the bundle, along with uglifying the
            // remaining code. I would prefer a non-uglifying tree-shaker, but there
            // isn't one, so this will have to do. This cuts the final bundle size
            // from approximately 3MB to approximately 900kB, as of this writing - a
            // substantial amount of page weight reduction.
            //
            // I still consider it far too bulky.
            new UglifyJSPlugin({
                sourceMap: true,
            }),
        ],
    },
}

const environment = environments[process.env.NODE_ENV || 'production'] || {}

module.exports = {
    entry: {
        // Guarantee that a Promise polyfill and a Fetch polyfill are available
        // as if they were browser globals by loading them as part of the
        // bundle.
        //
        // I looked at not using either, and browser tech has largely caught up
        // with these APIs, but it's still unreliable enough (according to
        // caniuse) that I opted to include the polyfills.
        app: ['babel-polyfill', 'whatwg-fetch', 'app'],
    },

    resolve: {
        modules: [
            'node_modules',
            resolve('ui'),
        ],
    },

    module: {
        rules: [
            // Enable Babel for all the ES2016 and JSX nicety. See .babelrc for
            // specific configuration.
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
            },
        ],
    },

    output: {
        // The chunk hash allows us to safely disregard caching without having
        // to consider the specifics of cache configuration, by treating each
        // newly-generated bundle as a unique resource which the client is very
        // unlikely to have ever seen before.
        filename: '[name].[chunkhash].js',
        path: resolve('static'),
    },

    plugins: Array.prototype.concat(
        environment.plugins || [],
        [
            // Inject the generated entry point name into index.html at build
            // time.
            new HtmlWebpackPlugin({
                filename: 'index.html',
                template: 'ui/index.html',
            }),
        ],
    ),

    devtool: 'source-map',
}
