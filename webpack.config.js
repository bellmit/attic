'use strict'

var path = require('path')
var keys = require('lodash.keys')
var webpack = require('webpack')
var autoprefixer = require('autoprefixer')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var ExtractTextPlugin = require("extract-text-webpack-plugin")

var thisPackage = require('./package.json')

module.exports = {
  entry: {
    app: ['app.less', 'app'],
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
        loader: ExtractTextPlugin.extract("css?sourceMap!postcss!less?sourceMap"),
      },
      {
        test: /\.(woff|woff2|svg|ttf|eot)$/,
        exclude: /node_modules/,
        loader: "file?name=[name].[hash].[ext]",
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

  postcss() {
    return [
      autoprefixer({
        browsers: [
          // Cribbed from Bootstrap's own autoprefixer list.
          'Android 2.3',
          'Android >= 4',
          'Chrome >= 20',
          'Firefox >= 24',
          'Explorer >= 8',
          'iOS >= 6',
          'Opera >= 12',
          'Safari >= 6',
        ],
      }),
    ]
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
      filename: '../index.html',
      template: 'src/index.html',
      chunksSortMode: 'dependency',
    }),
  ],

  devtool: '#source-map',
}
