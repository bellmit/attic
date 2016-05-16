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

    webpack: Object.assign({}, require('./webpack.config'), {
      entry: {},
      devtool: 'inline-source-map',
    }),

    reporters: ['progress'],

    browsers: ['Firefox'],

    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,

    autoWatch: true,
    singleRun: false,
  })
}
