package com.loginbox.heroku.http;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.server.SimpleServerFactory;

/**
 * A single-connector implementation of {@link io.dropwizard.server.ServerFactory}, suitable for PaaS deployments (e.g.,
 * Heroku) where applications are limited to a single, runtime-defined port. Applications can override the port by
 * setting the {@value com.loginbox.heroku.http.HerokuConnectorFactory#PORT_ENV_VAR} environment variable, or through
 * Dropwizard's system property overrides via {@code -Ddw.server.connector.port=1234}.
 * <p>
 * This server factory is provided by {@link com.loginbox.heroku.HerokuConfiguration} by default, but can be used in
 * other applications' YAML files without inheriting HerokuConfiguration:
 * <pre>
 * server:
 *     - type: heroku
 * </pre>
 * Other parameters, as documented for {@link io.dropwizard.server.SimpleServerFactory}, may be applied.
 *
 * @see com.loginbox.heroku.HerokuConfiguration
 * @see com.loginbox.heroku.http.HerokuConnectorFactory
 * @see io.dropwizard.server.ServerFactory
 * @see io.dropwizard.server.SimpleServerFactory
 * @see io.dropwizard.server.AbstractServerFactory
 */
@JsonTypeName("heroku")
public class HerokuServerFactory extends SimpleServerFactory {
    {
        setConnector(new HerokuConnectorFactory());
    }
}
