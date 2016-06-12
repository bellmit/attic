package com.unreasonent.ds;

import com.unreasonent.ds.auth.AuthBundle;
import com.unreasonent.ds.axon.AxonBundle;
import com.unreasonent.ds.cors.CorsBundle;
import com.unreasonent.ds.database.DatabaseBundle;
import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.sql.DataSource;

import static com.unreasonent.ds.bundle.BundleAdapter.wrap;

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
    private final MigrationsBundle<DistantShoreConfiguration> migrationsBundle
            = new MigrationsBundle<DistantShoreConfiguration>() {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(DistantShoreConfiguration configuration) {
            return configuration.getDatabase();
        }
    };
    private final AxonBundle axonBundle
            = new AxonBundle() {
        @Override
        protected DataSource getDataSource() {
            return databaseBundle.getDataSource();
        }
    };

    @Override
    public void initialize(Bootstrap<DistantShoreConfiguration> bootstrap) {
        bootstrap.addBundle(wrap(corsBundle));
        bootstrap.addBundle(wrap(authBundle));
        bootstrap.addBundle(wrap(databaseBundle));
        bootstrap.addBundle(wrap(migrationsBundle));
        bootstrap.addBundle(wrap(axonBundle));
    }

    @Override
    public void run(DistantShoreConfiguration configuration, Environment environment) throws Exception {
        /* No resources, just bundles */
    }
}
