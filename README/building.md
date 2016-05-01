# How do I build the server?

## Theory

The executable artefact is produced by the `app` project's `assemble` target, at `app/build/libs/distant-shore-all.jar`. This is an executable JAR (that is, it has a `Main-Class` manifest entry). This JAR contains both the backend - a Java program that exposes a web server - and the frontend - an HTML and Javascript app that talks to the server.

## Practice

Useful build targets:

* `./gradlew assemble`: build the binaries without running (most) checks.
* `./gradlew check`: run the test suite.
* `./gradlew build`: do both.

There's an additional build target, used on Heroku:

* `./gradlew stage`: build the Distant Shore shadow JAR, without running checks.

These targets build both the backend and the frontend, and integrate frontend tests into the process as well as possible.

## Frontend

Gradle is pretty good as a build orchestrator, and at building Java projects, but it's not the One True Build Tool. For some cases, another tool may make more sense - for example, this project uses Gulp (a Node-based build tool) to handle JS-driven asset compilation, since most of the tools are written in Node and since Gulp has fixtures for making things like source maps work well.

However, sometimes it's necessary to run those tools directly. For example, IDEA users will need to run `gulp` in the `app` project every time they change the stylesheets or frontend script packages used in Distant Shore before they'll be able to see those changes reflected in the app, as run from IDEA.

Useful targets in `app`:

* `gulp assets`: compile all of the frontend assets, placing them in Gradle's resources target for further packaging.
