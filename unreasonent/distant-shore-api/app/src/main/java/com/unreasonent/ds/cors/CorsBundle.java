package com.unreasonent.ds.cors;

import com.unreasonent.ds.DistantShoreConfiguration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.FilterRegistration;

public class CorsBundle implements ConfiguredBundle<DistantShoreConfiguration> {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(DistantShoreConfiguration configuration, Environment environment) throws Exception {
        configuration.getCors().registerFilter(environment.servlets());
    }
}
