package com.unreasonent.ds;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * The Distant Shore server. For simplicity, this uses bundles for all of the actual app logic.
 */
public class DistantShore extends Application<DistantShoreConfiguration> {
    public static void main(String[] args) throws Exception {
        new DistantShore().run(args);
    }

    @Override
    public void initialize(Bootstrap<DistantShoreConfiguration> bootstrap) {
    }

    @Override
    public void run(DistantShoreConfiguration configuration, Environment environment) throws Exception {
        /* No resources, just bundles */
    }
}
