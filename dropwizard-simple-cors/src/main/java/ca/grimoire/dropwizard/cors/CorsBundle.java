package ca.grimoire.dropwizard.cors;

import ca.grimoire.dropwizard.cors.config.CrossOriginFilterFactoryHolder;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Configures simple CORS handling for Dropwizard apps, based on origins configured by a
 * {@link ca.grimoire.dropwizard.cors.config.CrossOriginFilterFactory} in the application configuration class.
 * <p>
 * To use this bundle in your application:
 * <ol>
 *     <li>Implement {@link CrossOriginFilterFactoryHolder} in your configuration class.</li>
 *     <li>Register this bundle in your application class' initialize method.</li>
 *     <li>Add origins to your application configuration file.</li>
 * </ol>
 *
 * @param <C> the application configuration class, which must provide a CORS configuration.
 */
public class CorsBundle<C extends CrossOriginFilterFactoryHolder>
        implements ConfiguredBundle<C> {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        configuration.getCors().registerFilter(environment.servlets());
    }
}
