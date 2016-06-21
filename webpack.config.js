'use strict';

var path = require('path');
var keys = require('lodash.keys');
var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var thisPackage = require('./package.json');

module.exports = {
  entry: {
    app: 'app',
    vendor: keys(thisPackage.dependencies),
  },

  resolve: {
    root: [
      path.resolve("src"),
    ],
    // Automatically resolve JSX modules, like JS modules.
    extensions: ["", ".webpack.js", ".web.js", ".js", ".jsx"],
  },

  output: {
    path: path.resolve("dist/bundle"),
    publicPath: "/bundle/",
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
        test: /\.less$/,
        exclude: /node_modules/,
        loader: ExtractTextPlugin.extract("css?sourceMap!less?sourceMap"),
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
    new ExtractTextPlugin("[name].[contenthash].css"),
    new HtmlWebpackPlugin({
      // put index.html outside the bundle/ subdir
      filename: '../index.html',
      template: 'src/index.html',
      chunksSortMode: 'dependency',
    }),
  ],

  devtool: '#source-map',
};
