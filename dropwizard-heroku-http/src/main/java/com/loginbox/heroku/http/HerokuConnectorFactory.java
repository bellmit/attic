package com.loginbox.heroku.http;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.jetty.HttpConnectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An HTTP connector that will automatically configure itself using Heroku's environment variables, if provided.
 * <p>
 * This connector respects all configuration entries from {@link io.dropwizard.jetty.HttpConnectorFactory}, as well as
 * the {@value #PORT_ENV_VAR} environment variable. If set, the environment variable takes precedence.
 * <p>
 * A HerokuConnectorFactory is automatically provided by {@link HerokuServerFactory}, but you
 * can use this in your own configuration YAML directly:
 * <pre>
 * server:
 *     # ...type, etc...
 *     connectors:
 *         - type: heroku
 * </pre>
 *
 * @see com.loginbox.heroku.config.HerokuConfiguration
 * @see HerokuServerFactory
 * @see io.dropwizard.jetty.ConnectorFactory
 * @see io.dropwizard.jetty.HttpConnectorFactory
 */
@JsonTypeName("heroku")
public class HerokuConnectorFactory extends HttpConnectorFactory {

    /**
     * The environment variable holding the HTTP port number for this app. If this variable is not set, this connector
     * will use the {@link #DEFAULT_PORT}. If set, and not a number, configuration will fail.
     */
    public static final String PORT_ENV_VAR = "PORT";

    /**
     * The default HTTP port, used if
     */
    public static final int DEFAULT_PORT = 5000;

    private static final Logger LOG = LoggerFactory.getLogger(HerokuConnectorFactory.class);

    /**
     * Obtain and return the port to use for this configuration. If {@value #PORT_ENV_VAR} is set in the environment,
     * this will convert it to an integer and use it (or return 0, if the value is not an integer). Otherwise, this will
     * return {@link #DEFAULT_PORT}.
     *
     * @return the application port number.
     */
    public static int applicationPort() {
        String environmentPort = System.getenv(PORT_ENV_VAR);
        if (environmentPort != null) {
            try {
                int port = Integer.parseInt(environmentPort);
                return port;
            } catch (NumberFormatException nfe) {
                LOG.error("PORT environment variable set, but not a number: {}", environmentPort);
                return 0;
            }
        }
        return DEFAULT_PORT;
    }

    {
        setPort(applicationPort());
    }
}
