# Bootstrap with Maven and RequireJS Sample

## Structure

* HTML pages and static resources in `src/main/webapp`
* JS sources in `src/main/js`, including runtime config for RequireJS
    * `site.js` is the "main" script for the example page, and includes some
      example code
    * Output in `js/` in the final WAR, including "optimized" copies of input
      scripts as well as combined version of `site.js`
* RequireJS configuration and tooling in `src/main/requirejs`.
    * `build.js` contains settings for the optimizer.
    * `r.js` contains an up-to-date version of the optimizer, since the Maven
      plugin is mostly unmaintained.
* Less sources in `src/main/less`
    * `site.less` is the "main" stylesheet for the example page
    * Output in `css/` in the final WAR

## jQuery.noConflict

[RequireJS docs](http://requirejs.org/docs/jquery.html) suggest using `map`
and a loader shim to keep JQuery out of the global namespace. I've opted _not_
to do this, since it interferes with Bootstrap's ability to extend JQuery.
Shim configuration isn't quite sophisticated enough.

## Versions

* jQuery 2.0.3
* RequireJS 2.1.8
* Bootstrap 2.3.2

## Package and run

    mvn org.mortbay.jetty:jetty-maven-plugin:run-war

Then visit http://localhost:8080/

Enjoy!