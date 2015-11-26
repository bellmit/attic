var gulp = require('gulp');
var less = require('gulp-less-sourcemap');
var path = require('path');

gulp.task('less', function () {
    var lessSource = path.join("src", "main", "less");
    var lessRoots = path.join(lessSource, "*.less");
    var lessOutput = path.join("src", "main", "resources", "assets", "css");

    gulp
        .src(lessRoots)
        .pipe(less({
            sourceMap: {
                outputSourceFiles: true
            }
        }))
        .pipe(gulp.dest(lessOutput));
});

gulp.task('assets', ['less']);
