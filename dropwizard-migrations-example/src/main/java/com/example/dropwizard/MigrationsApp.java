package com.example.dropwizard;

import com.example.dropwizard.resources.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MigrationsApp extends Application<MigrationsAppConfiguration> {
    public static void main(String... args) throws Exception {
        new MigrationsApp().run(args);
    }

    private MigrationsBundle<MigrationsAppConfiguration> migrationsBundle = new MigrationsBundle<MigrationsAppConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(MigrationsAppConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    private HibernateBundle<MigrationsAppConfiguration> hibernateBundle = new ScanningHibernateBundle<MigrationsAppConfiguration>("com.example.dropwizard.api") {
        @Override
        public DataSourceFactory getDataSourceFactory(MigrationsAppConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<MigrationsAppConfiguration> bootstrap) {
        bootstrap.addBundle(migrationsBundle);
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(MigrationsAppConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new HelloResource(hibernateBundle.getSessionFactory()));
    }
}
