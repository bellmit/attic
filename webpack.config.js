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
    filename: "[name].bundle.js",
    // Including chunk hashes helps prevent loader shear if a deployment
    // catches you in the middle of a session. A 404 is a better outcome than
    // silently receiving the wrong source code.
    chunkFilename: "chunks/[name].[chunkhash].chunk.js",
    publicPath: "/js/",
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
    new webpack.optimize.MinChunkSizePlugin({
      // Best guess: combine chunks under 100kb. Needs tweaking.
      minChunkSize: 100 * 1024,
    }),
  ],

  devtool: '#source-map',
};
