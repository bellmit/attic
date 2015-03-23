# Dropwizard Heroku Support

[![Build Status](https://circleci.com/gh/login-box/dropwizard-heroku.svg)](https://circleci.com/gh/login-box/dropwizard-heroku) [ ![Download](https://api.bintray.com/packages/login-box/releases/dropwizard-heroku/images/download.svg) ](https://bintray.com/login-box/releases/dropwizard-heroku/_latestVersion)

Using [Dropwizard](https://dropwizard.io/) on Heroku? This module takes care of
some common cases automatically.

## Installing

All modules are hosted on Bintray's JCenter repository.

To add to Maven:

    <repositories>
        <!-- ... -->
        <repository>
            <id>jcenter</id>
            <url>https://jcenter.bintray.com/</url>
        </repository>
    </repository>

To add to Gradle:

    repositories {
        jcenter()
    }

You can also download the JARs by hand from
[Bintray](https://bintray.com/login-box/releases/dropwizard-heroku/view#files),
but you'll need Dropwizard and its dependencies, too. Really, use a dependency
manager. You'll be happier.

## Easy Mode

The `dropwizard-heroku-config` module provides a configuration base class that
automatically applies the [Logging](#logging) and [HTTP](#http) configurations
below, without requiring any additional config file entries.

To use, add `com.loginbox.heroku:dropwizard-heroku-config:+` to your
dependencies, then subclass `com.loginbox.heroku.config.HerokuConfiguration` in
your application config:

```java
import com.loginbox.heroku.config.HerokuConfiguration;

public class ExampleConfiguration extends HerokuConfiguration {
    /* your configuration fields here */
}
```

If you prefer more control, read on…

## PostgreSQL Databases

The `dropwizard-heroku-db` module provides automatic detection of databases
configured using Heroku's PostgreSQL add-ons.

To use, add `com.loginbox.heroku:dropwizard-heroku-db:+` to your dependencies,
then add a `com.loginbox.heroku.db.HerokuDataSourceFactory` to your application
config class:

```java
import io.dropwizard.Configuration;
import com.loginbox.heroku.db.HerokuDataSourceFactory;

public class ExampleConfiguration extends Configuration {
    @NotNull
    @Valid
    private HerokuDataSourceFactory dataSourceFactory = new HerokuDataSourceFactory();

    @JsonProperty("database")
    public HerokuDataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }
}
```

The resulting configuration can be used with Dropwizard's usual database access
layers.

## Logging

Heroku expects applications to log to standard output, and provides its own log
handling to add things like source attribution and timestamps. The default
Dropwizard logging is a bit overzealous, and adds a lot of the same
information. The `dropwizard-heroku-logging` module provides a more Heroku-appropriate behaviour.

To use, add `com.loginbox.heroku:dropwizard-heroku-logging:+` to your
dependencies, then add

```yaml
logging:
    appenders:
        - type: heroku
```

to your Dropwizard application config.

Logs using the Heroku appender will include the severity, logger name, message,
and (for exceptions) the exception and stack trace. For example:

    INFO  org.eclipse.jetty.server.Server: Started @2775ms

In combination with Heroku's own log decorations, this becomes

    2015-03-20T02:22:14.921851+00:00 app[web.1]: INFO  org.eclipse.jetty.server.Server: Started @4471ms

Note that Heroku occasionally reorders log messages, which can mangle
multi-line messages such as stack traces. It will also interleave logs from
multiple dynos.

## HTTP

Heroku passes HTTP configuration to your app via the `PORT` environment
variable. Dropwizard, normally, expects the HTTP port to be set via a config
file, or using an awkward `-Ddw.…` system property. The
`dropwizard-heroku-http` module automates the process, providing some sane
defaults.

To use, add `com.loginbox.heroku:dropwizard-heroku-http:+` to your
dependencies, then add

```yaml
server:
    - type: heroku
```

The `heroku` server factory is a subclass of Dropwizard's own
[SimpleServerFactory](http://dropwizard.github.io/dropwizard/0.8.0/dropwizard-co
re/apidocs/io/dropwizard/server/SimpleServerFactory.html), and behaves much the
same way. Both the application context (at `/`) and the admin context (at
`/!/admin/`) are served on the same listener. The port _automatically_ defaults
to `$PORT`, if set, or to `5000` otherwise.

This also disables Dropwizard's default request logging. Heroku's routing stack
logs the same information.
