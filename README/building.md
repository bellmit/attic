# How do I build the server?

## Theory

The executable artefact is produced by the `app` project's `assemble` target, at `app/build/libs/distant-shore-all.jar`. This is an executable JAR (that is, it has a `Main-Class` manifest entry).

## Practice

Useful build targets:

* `./gradlew assemble`: build the binaries without running (most) checks.
* `./gradlew check`: run the test suite.
* `./gradlew build`: do both.

There's an additional build target, used on Heroku:

* `./gradlew stage`: build the Distant Shore shadow JAR, without running checks.
