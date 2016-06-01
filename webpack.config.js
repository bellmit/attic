'use strict';

var path = require('path');
var webpack = require('webpack');

module.exports = {
  entry: {
    app: "app.js",
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
    filename: "[name].bundle.js",
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
  ],

  devtool: '#source-map',
};
