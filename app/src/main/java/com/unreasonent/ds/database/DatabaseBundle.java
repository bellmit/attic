package com.unreasonent.ds.database;

import com.unreasonent.ds.DistantShoreConfiguration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;

import javax.sql.DataSource;

public class DatabaseBundle implements ConfiguredBundle<DistantShoreConfiguration> {
    private ManagedDataSource dataSource = null;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(DistantShoreConfiguration configuration, Environment environment) throws Exception {
        dataSource = configuration.getDatabase()
                .build(environment.metrics(), "distant-shore-api");
        environment.lifecycle().manage(dataSource);

        QueryHealthCheck healthCheck = new QueryHealthCheck(
                environment.getHealthCheckExecutorService(),
                Duration.seconds(5),
                dataSource);
        environment.healthChecks().register("db", healthCheck);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
