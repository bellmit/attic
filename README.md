# Dropwizard Simple CORS

A bundle providing simple CORS configuration for your Dropwizard apps.

This bundle configures Jetty's built-in `CorsFilter` to allow all requests and
methods from a list of known origins, which may include wildcards. The origins
can be configured either in your application's config file, or in an
environment variable for platforms such as Heroku.

## Installation

This library is hosted on [JCenter](https://bintray.com/bintray/jcenter). To
add it to your project, first add the JCenter repository, then add the
following dependency:

* Maven:

    ```xml
    <dependency>
        <groupId>ca.grimoire.dropwizard.cors</groupId>
        <artifactId>dropwizard-simple-cors</artifactId>
        <version>VERSION</version>
    </dependency>
    ```

* Gradle:

    ```groovy
    dependencies {
        # ...
        compile 'ca.grimoire.dropwizard.cors:dropwizard-simple-cors:*'
    }
    ```

## Usage

1. Add this module as a dependency for your application.

2. Implement `CrossOriginFilterFactoryHolder` on your configuration class.

3. Register `CorsBundle` in your application class.

4. Configure the list of origins you wish to authorize, either in your
    configuration file or through the `CORS_ORIGINS` environment variable.

Your application will respond both to simple CORS requests and to CORS
preflight (`OPTIONS`) requests, and authorize them if they originate from one
of the allowed origins.

## Origins

An origin is a protocol name and an authority, as in a URL: `proto://host`, or
`proto://host:port`. The authority (`host:port` part) may contain the wildcard
`*`, which matches any string of characters and may span multiple segments of a
domain name or port number.

Each provided origin will be tested against the `Origin` header of each
incoming CORS request. If the origin matches, the request is authorized.

For development, the default list of allowed origins contains only
`http://localhost:*`, to allow front-end applications hosted locally to call
your service. For deployment, you _must_ set the list of allowed origins to a
sensible value.
