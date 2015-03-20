# Publishing Builds

## Configuration

You'll need to set up your Bintray credentials before performing a release
build. In `~/.gradle/gradle.properties`, add the following keys:

    bintrayUsername=your-username-here
    bintrayApiKey=your-api-key-here

As these credentials are password-equivalent, you will also want to run

    chmod 0600 ~/.gradle/gradle.properties

## Process

1. Ensure you have all tags:

        git fetch origin --tags

2. Tag the release:

        git tag -a v0.1.0

    Put a brief release note in the tag message. Keep it short.

3. Push the tag:

        git push origin v0.1.0

4. Build and publish to Bintray:

        gradle bintrayUpload

    This will upload, but _not publish_, the new build.

5. Publish the build in the
    [Bintray web UI](https://bintray.com/login-box/releases/dropwizard-heroku/).
