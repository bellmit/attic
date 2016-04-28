package com.unreasonent.ds;

import com.unreasonent.ds.frontend.FrontendBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

/**
 * The Distant Shore server. For simplicity, this uses bundles for all of the actual app logic.
 */
public class DistantShore extends Application<DistantShoreConfiguration> {
    public static void main(String[] args) throws Exception {
        new DistantShore().run(args);
    }

    private final ViewBundle<DistantShoreConfiguration> viewBundle
            = new ViewBundle<>();
    private final FrontendBundle frontendBundle
            = new FrontendBundle();

    @Override
    public void initialize(Bootstrap<DistantShoreConfiguration> bootstrap) {
        bootstrap.addBundle(viewBundle);
        bootstrap.addBundle(frontendBundle);
    }

    @Override
    public void run(DistantShoreConfiguration configuration, Environment environment) throws Exception {
        /* No resources, just bundles */
    }
}
