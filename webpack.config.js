const path = require('path')
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')

// Assume production unless specified, so that env-sensitive things get
// prod-like behaviours by default. Minimize confusion.
const environment = process.env.NODE_ENV || 'production'

// Plugins, based on NODE_ENV (production by default)
const basePlugins = [
  new webpack.DefinePlugin({
    'process.env': {
      'NODE_ENV': JSON.stringify(environment)
    }
  }),
  new HtmlWebpackPlugin({
    template: 'index.html',
  }),
]
const environmentPlugins = {
  development: [],
  production: [
    new webpack.optimize.UglifyJsPlugin({
      sourceMap: true,
    }),
  ],
}
const plugins = [
  ...basePlugins,
  ...environmentPlugins[environment],
]

// Actual webpack config
module.exports = {
  context: path.resolve('src'),
  entry: {
    index: '.',
  },

  output: {
    path: path.resolve('webroot'),
    filename: 'bundle/[name].[hash].js',
  },

  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loaders: [
          'babel-loader',
        ],
      },
    ],
  },

  plugins,

  devtool: '#source-map',
}
