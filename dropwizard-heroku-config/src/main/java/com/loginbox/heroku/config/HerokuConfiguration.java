package com.loginbox.heroku.config;

import com.loginbox.heroku.http.HerokuServerFactory;
import com.loginbox.heroku.logging.HerokuLoggingFactory;
import com.loginbox.heroku.http.HerokuServerFactory;
import com.loginbox.heroku.logging.HerokuLoggingFactory;
import io.dropwizard.Configuration;

/**
 * A configuration with appropriate defaults for the Heroku platform. This automatically configures HTTP to respect
 * Heroku's environment variables, and configures logging to expect Heroku to handle timestamps and process names. As
 * with Configuration, subclasses can be loaded from JSON or YAML.
 * <p>
 * For further information on Dropwizard configuration and YAML, see {@link io.dropwizard.Configuration}'s
 * documentation.
 *
 * @see io.dropwizard.Configuration
 * @see com.loginbox.heroku.http.HerokuServerFactory
 * @see com.loginbox.heroku.logging.HerokuLoggingFactory
 */
public class HerokuConfiguration extends Configuration {
    {
        setServerFactory(new HerokuServerFactory());
        setLoggingFactory(new HerokuLoggingFactory());
    }
}
