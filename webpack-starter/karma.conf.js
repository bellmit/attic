var webpackConfig = require('./webpack.config')

module.exports = function(config) {
  config.set({
    basePath: '',

    frameworks: ['mocha', 'chai'],

    files: [
      'test/bundle.js',
    ],

    preprocessors: {
      'test/bundle.js': ['webpack', 'sourcemap'],
    },

    webpack: Object.assign({}, webpackConfig, {
      entry: {},
      plugins: [],
      devtool: 'inline-source-map',
      module: Object.assign({}, webpackConfig.module, {
        loaders: webpackConfig.module.loaders.concat([
          {
            test: /\.json$/,
            loader: 'json',
          },
        ]),
      }),
      externals: {
        'cheerio': 'window',
        'react/addons': true,
        'react/lib/ExecutionEnvironment': true,
        'react/lib/ReactContext': true,
      },
    }),

    webpackMiddleware: {
      noInfo: true,
    },

    reporters: ['progress'],

    browsers: ['Firefox'],

    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,

    autoWatch: true,
    singleRun: false,
  })
}
