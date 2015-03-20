package com.loginbox.heroku.http;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.server.SimpleServerFactory;

/**
 * A single-connector implementation of {@link io.dropwizard.server.ServerFactory}, suitable for PaaS deployments (e.g.,
 * Heroku) where applications are limited to a single, runtime-defined port. Applications can override the port by
 * setting the {@value HerokuConnectorFactory#PORT_ENV_VAR} environment variable, or through
 * Dropwizard's system property overrides via {@code -Ddw.server.connector.port=1234}.
 * <p>
 * As with {@link io.dropwizard.server.SimpleServerFactory}, this server factory serves both the application and the admin endpoints on the same connector, at the following prefixes:
 * <table>
 *     <thead>
 *         <tr>
 *             <th>Component</th>
 *             <th>Path</th>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td>Application</td>
 *             <td><code>{@value #APP_CONTEXT_PATH}</code></td>
 *         </tr>
 *         <tr>
 *             <td>Admin</td>
 *             <td><code>{@value #ADMIN_CONTEXT_PATH}</code></td>
 *         </tr>
 *     </tbody>
 * </table>
 * The <code>{@value #RESERVED_PREFIX}</code> URL namespace is reserved for use by Dropwizard components, and should not
 * be used by application resources.
 * <p>
 * This server factory is provided by {@link com.loginbox.heroku.config.HerokuConfiguration} by default, but can be used in
 * other applications' YAML files without inheriting HerokuConfiguration:
 * <pre>
 * server:
 *     - type: heroku
 * </pre>
 * Other parameters, as documented for {@link io.dropwizard.server.SimpleServerFactory}, may be applied.
 *
 * @see com.loginbox.heroku.config.HerokuConfiguration
 * @see HerokuConnectorFactory
 * @see io.dropwizard.server.ServerFactory
 * @see io.dropwizard.server.SimpleServerFactory
 * @see io.dropwizard.server.AbstractServerFactory
 */
@JsonTypeName("heroku")
public class HerokuServerFactory extends SimpleServerFactory {
    /**
     * The default root path for applications served out of this server factory.
     */
    public static final String APP_CONTEXT_PATH = "/";

    /**
     * All Dropwizard internal applications will be served under this prefix, to avoid collisions with your application.
     * To avoid conflicts, do not use URLs starting with this prefix in your code.
     */
    public static final String RESERVED_PREFIX = "/!/";

    /**
     * The default root path for the Dropwizard admin application, when served out of this server factory.
     */
    public static final String ADMIN_CONTEXT_PATH = RESERVED_PREFIX + "admin/";

    {
        setConnector(new HerokuConnectorFactory());
        setApplicationContextPath(APP_CONTEXT_PATH);
        setAdminContextPath(ADMIN_CONTEXT_PATH);
    }
}
