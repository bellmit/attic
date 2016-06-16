'use strict';

var path = require('path');
var keys = require('lodash.keys');
var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');

var thisPackage = require('./package.json');

module.exports = {
  entry: {
    app: "app.js",
    vendor: keys(thisPackage.dependencies),
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
      {
        test: /node_modules[\\\/]auth0-lock[\\\/].*\.js$/,
        loaders: [
          'transform-loader/cacheable?brfs',
          'transform-loader/cacheable?packageify',
        ],
      },
      {
        test: /node_modules[\\\/]auth0-lock[\\\/].*\.ejs$/,
        loader: 'transform-loader/cacheable?ejsify',
      },
      {
        test: /\.json$/,
        loader: 'json',
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
