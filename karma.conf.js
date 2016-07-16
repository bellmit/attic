var reporters = ['progress']

var circleTestDir = process.env.CIRCLE_TEST_REPORTS
if (circleTestDir) {
  reporters.push('junit')
}

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
      plugins: [],
      devtool: 'inline-source-map',
    }),

    reporters: reporters,

    browsers: ['Firefox'],

    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,

    autoWatch: true,
    singleRun: false,

    junitReporter: {
      outputDir: circleTestDir || '',
    },
  })
}
