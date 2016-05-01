'use strict';

var gulp = require('gulp');
var sourcemaps = require('gulp-sourcemaps');
var less = require('gulp-less');
var postcss = require('gulp-postcss');
var autoprefixer = require('autoprefixer');
var path = require('path');

var inputs = path.resolve("src/main");
// Write directly into Gradle's (and Idea's) build output, to avoid issues with generated files in src/.
var assets = path.resolve("build/resources/main/assets")

gulp.task('style', function () {
    var lessSource = path.join(inputs, "less");
    var lessRoots = path.join(lessSource, "*.less");
    var lessOutput = path.join(assets, "css");

    // Cribbed shamelessly from Bootstrap's own browser list:
    //    https://github.com/twbs/bootstrap/blob/614559b41ab71cba318b97c8e7e8277917d4fdde/grunt/configBridge.json
    var bootstrapBrowsers = [
        "Android 2.3",
        "Android >= 4",
        "Chrome >= 20",
        "Firefox >= 24",
        "Explorer >= 8",
        "iOS >= 6",
        "Opera >= 12",
        "Safari >= 6",
    ];

    gulp
        .src(lessRoots)
        .pipe(sourcemaps.init())
        .pipe(less())
        .pipe(postcss([ autoprefixer({ browsers: bootstrapBrowsers }) ]))
        // Separate map files only get loaded when a user opens the inspector, keeping latency and throughput down for
        // the common case of "I'm just using the site."
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(lessOutput));
});

gulp.task('assets', ['style']);
