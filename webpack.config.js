'use strict';

var path = require('path');
var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  entry: {
    app: "app.js",
    vendor: [
      "classnames",
      "detect-dom-ready",
      "flux-standard-action",
      "lodash",
      "react",
      "react-dom",
      "react-document-title",
      "react-redux",
      "react-router",
      "redux",
      "redux-actions",
    ],
  },

  resolve: {
    root: [
      path.resolve("js"),
    ],
    // Automatically resolve JSX modules, like JS modules.
    extensions: ["", ".webpack.js", ".web.js", ".js", ".jsx"],
  },

  output: {
    path: path.resolve("dist/js"),
    publicPath: "/js/",
    filename: "[name].[chunkhash].js",
  },

  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: "babel",
        query: {
          presets: ['es2015'],
          plugins: ['transform-object-rest-spread'],
        },
      },
      {
        test: /\.jsx$/,
        exclude: /node_modules/,
        loader: "babel",
        query: {
          presets: ['react', 'es2015'],
          plugins: ['transform-object-rest-spread'],
        },
      },
      {
        test: /\.yaml$/,
        exclude: /node_modules/,
        loader: "json!yaml",
      },
    ],
  },

  plugins: [
    new webpack.optimize.OccurrenceOrderPlugin(/* preferEntry=*/true),
    new webpack.optimize.CommonsChunkPlugin({
      name: 'vendor',
      minChunks: Infinity,
    }),
    new webpack.optimize.CommonsChunkPlugin({
      name: 'boot',
      chunks: ['vendor'],
    }),
    new HtmlWebpackPlugin({
      title: "Distant Shore",
      // escape the js/ subdir
      filename: '../index.html',
      template: 'html/index.html',
      inject: 'head',
      chunksSortMode: 'dependency',
    }),
  ],

  devtool: '#source-map',
};
