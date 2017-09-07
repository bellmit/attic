# Dropwizard Simple CORS

A bundle providing simple CORS configuration for your Dropwizard apps.

This bundle configures Jetty's built-in `CorsFilter` to allow all requests and
methods from a list of known origins, which may include wildcards. The origins
can be configured either in your application's config file, or in an
environment variable for platforms such as Heroku.

## Usage

1. Add this module as a dependency for your application.

    This library is hosted on [JCenter](https://bintray.com/bintray/jcenter). To
    add it to your project, first add the JCenter repository:

    * Maven:

        ```xml
        <repositories>
            <repository>
                <id>jcenter</id>
                <url>https://jcenter.bintray.com/</url>
            </repository>
        </repositories>
        ```

    * Gradle:

        ```groovy
        repositories {
            jcenter()
        }
        ```
    Then add the following dependency:

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
            compile group: 'ca.grimoire.dropwizard.cors', name: 'dropwizard-simple-cors', version: 'VERSION'
        }
        ```
    Replacing `VERSION` with the version you wish to use.

2. Implement `CrossOriginFilterFactoryHolder` on your configuration class.

    ```Java
    import io.dropwizard.Configuration;
    import ca.grimoire.dropwizard.cors.config.CrossOriginFilterFactory;
    import ca.grimoire.dropwizard.cors.config.CrossOriginFilterFactoryHolder;

    public class MyConfiguration extends Configuration implements CrossOriginFilterFactoryHolder {
        private CrossOriginFilterFactory cors = new CrossOriginFilterFactory();

        public void setCors(CrossOriginFilterFactory cors) {
            this.cors = cors;
        }

        @Override
        public CrossOriginFilterFactory getCors() {
            return cors;
        }
    }
    ```

3. Register `CorsBundle` in your application class.

    ```Java
    import ca.grimoire.dropwizard.cors.CorsBundle;

    import io.dropwizard.Application;
    import io.dropwizard.setup.Bootstrap;

    public class MyApplication extends Application<MyConfiguration> {

        @Override
        public void initialize(Bootstrap<MyConfiguration> bootstrap) {
            bootstrap.addBundle(new CorsBundle<MyConfiguration>());
        }

    }
    ```

4. Configure the list of origins you wish to authorize, either in your
    configuration file or through the `CORS_ORIGINS` environment variable.

    ```yaml
    cors:
      origins: "*"
    ```

    I make no statements about the advisability of configuring CORS that way. Your needs may differ.

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
