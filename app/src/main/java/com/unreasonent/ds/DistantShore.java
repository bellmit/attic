package com.unreasonent.ds;

import com.unreasonent.ds.auth.AuthBundle;
import com.unreasonent.ds.cors.CorsBundle;
import com.unreasonent.ds.database.DatabaseBundle;
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

    private final CorsBundle corsBundle
            = new CorsBundle();
    private final AuthBundle authBundle
            = new AuthBundle();
    private final DatabaseBundle databaseBundle
            = new DatabaseBundle();

    @Override
    public void initialize(Bootstrap<DistantShoreConfiguration> bootstrap) {
        bootstrap.addBundle(corsBundle);
        bootstrap.addBundle(authBundle);
        bootstrap.addBundle(databaseBundle);
    }

    @Override
    public void run(DistantShoreConfiguration configuration, Environment environment) throws Exception {
        /* No resources, just bundles */
    }
}
