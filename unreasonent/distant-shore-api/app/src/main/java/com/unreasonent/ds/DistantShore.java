package com.unreasonent.ds;

import com.loginbox.dropwizard.mybatis.DatasourceMybatisBundle;
import com.loginbox.dropwizard.mybatis.MybatisBundle;
import com.unreasonent.ds.auth.AuthBundle;
import com.unreasonent.ds.axon.AxonBundle;
import com.unreasonent.ds.cors.CorsBundle;
import com.unreasonent.ds.database.DatabaseBundle;
import com.unreasonent.ds.squad.SquadBundle;
import io.dropwizard.Application;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcedAggregateRoot;
import org.axonframework.eventsourcing.EventSourcingRepository;

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
    private final DatasourceMybatisBundle mybatisBundle
            = new DatasourceMybatisBundle("com.unreasonent.ds") {
        @Override
        protected DataSource getDataSource() {
            return databaseBundle.getDataSource();
        }
    };
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
    private final SquadBundle squadBundle = new SquadBundle() {
        @Override
        protected <T extends EventSourcedAggregateRoot<?>> EventSourcingRepository<T> newRepository(Class<T> aggregateClass) {
            return axonBundle.newRepository(aggregateClass);
        }

        @Override
        public DefaultCommandGateway getCommandGateway() {
            return axonBundle.getCommandGateway();
        }

        @Override
        public CommandBus getCommandBus() {
            return axonBundle.getCommandBus();
        }

        @Override
        protected EventBus getEventBus() {
            return axonBundle.getEventBus();
        }

        @Override
        protected SqlSessionFactory getSqlSessionFactory() {
            return mybatisBundle.getSqlSessionFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<DistantShoreConfiguration> bootstrap) {
        bootstrap.addBundle(wrap(corsBundle));
        bootstrap.addBundle(wrap(authBundle));
        bootstrap.addBundle(wrap(databaseBundle));
        bootstrap.addBundle(wrap(mybatisBundle));
        bootstrap.addBundle(wrap(migrationsBundle));
        bootstrap.addBundle(wrap(axonBundle));
        bootstrap.addBundle(wrap(squadBundle));
    }

    @Override
    public void run(DistantShoreConfiguration configuration, Environment environment) throws Exception {
        /* No resources, just bundles */
    }
}
