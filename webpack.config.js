const path = require('path')
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
  context: path.join(__dirname, 'src'),
  entry: {
    index: '.',
  },

  output: {
    path: path.join(__dirname, 'webroot'),
    filename: 'bundle/[name].[hash].js',
  },

  plugins: [
    new HtmlWebpackPlugin({
      template: 'index.html',
    }),
    new webpack.optimize.UglifyJsPlugin({
      sourceMap: true,
    }),
  ],

  devtool: '#source-map',
}
