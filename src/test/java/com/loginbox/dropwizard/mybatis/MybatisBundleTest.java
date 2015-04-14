package com.loginbox.dropwizard.mybatis;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.loginbox.dropwizard.mybatis.healthchecks.SqlSessionFactoryHealthCheck;
import com.loginbox.dropwizard.mybatis.mappers.Ping;
import com.loginbox.dropwizard.mybatis.testMappers.ExampleMapper;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Bootstrap;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.UUID;
import java.util.Optional;

import static com.loginbox.dropwizard.mybatis.matchers.ConfigurationMapperMatcher.hasMapper;
import static com.loginbox.dropwizard.mybatis.matchers.ConfigurationTypeHandlerMatcher.hasTypeHandler;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MybatisBundleTest {
    private final DataSourceFactory dataSourceFactory = mock(DataSourceFactory.class);
    private final Bootstrap<io.dropwizard.Configuration> bootstrap = mock(Bootstrap.class);
    private final io.dropwizard.Configuration configuration = mock(io.dropwizard.Configuration.class);
    private final io.dropwizard.setup.Environment environment = mock(io.dropwizard.setup.Environment.class);
    private final ManagedDataSource dataSource = mock(ManagedDataSource.class);
    private final LifecycleEnvironment lifecycle = mock(LifecycleEnvironment.class);
    private final HealthCheckRegistry healthChecks = mock(HealthCheckRegistry.class);

    private class TestMybatisBundle<T extends io.dropwizard.Configuration>
            extends MybatisBundle<T> {
        public TestMybatisBundle() {
            super();
        }

        public TestMybatisBundle(Class<?> mapper, Class<?>... mappers) {
            super(mapper, mappers);
        }

        public TestMybatisBundle(String packageName, String... packageNames) {
            super(packageName, packageNames);
        }

        @Override
        public DataSourceFactory getDataSourceFactory(io.dropwizard.Configuration configuration) {
            return dataSourceFactory;
        }
    }

    @Before
    public void wireMocks() {
        when(environment.lifecycle()).thenReturn(lifecycle);
        when(environment.healthChecks()).thenReturn(healthChecks);

        when(dataSourceFactory.build(Matchers.any(), Matchers.any())).thenReturn(dataSource);
    }

    @Test
    public void managesDataSource() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle
                = new TestMybatisBundle<io.dropwizard.Configuration>();
        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        verify(lifecycle).manage(dataSource);
    }

    @Test
    public void usesDataSource() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle
                = new TestMybatisBundle<io.dropwizard.Configuration>();
        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        assertThat(bundle.getSqlSessionFactory().getConfiguration().getEnvironment().getDataSource(), is(dataSource));
    }

    @Test
    public void registersHealthChecks() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle
                = new TestMybatisBundle<io.dropwizard.Configuration>();
        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        verify(healthChecks).register(anyString(), isA(SqlSessionFactoryHealthCheck.class));
    }

    @Test
    public void registersInterfaces() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle
                = new TestMybatisBundle<io.dropwizard.Configuration>(ExampleMapper.class);
        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        Configuration configuration = bundle.getSqlSessionFactory().getConfiguration();

        assertThat(configuration, hasMapper(ExampleMapper.class));
    }

    @Test
    public void registersPackages() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle
                = new TestMybatisBundle<io.dropwizard.Configuration>("com.loginbox.dropwizard.mybatis.testMappers");
        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        Configuration configuration = bundle.getSqlSessionFactory().getConfiguration();

        assertThat(configuration, hasMapper(ExampleMapper.class));
    }

    @Test
    public void registersPingMapper() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle
                = new TestMybatisBundle<io.dropwizard.Configuration>();
        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        Configuration configuration = bundle.getSqlSessionFactory().getConfiguration();

        assertThat(configuration, hasMapper(Ping.class));
    }

    @Test
    public void registersUuidType() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle
                = new TestMybatisBundle<io.dropwizard.Configuration>();
        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        Configuration configuration = bundle.getSqlSessionFactory().getConfiguration();

        assertThat(configuration, hasTypeHandler(UUID.class));
    }

    @Test
    public void registersJava8OptionalType() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle
                = new TestMybatisBundle<io.dropwizard.Configuration>();
        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        Configuration configuration = bundle.getSqlSessionFactory().getConfiguration();

        assertThat(configuration, hasTypeHandler(Optional.class));
    }

    @Test
    public void appliesCustomConfiguration() throws Exception {
        MybatisBundle<io.dropwizard.Configuration> bundle = new TestMybatisBundle<io.dropwizard.Configuration>(ExampleMapper.class) {
            @Override
            protected void configureMybatis(Configuration configuration) {
                configuration.setDatabaseId("--test-value--");
            }
        };

        bundle.initialize(bootstrap);
        bundle.run(configuration, environment);

        Configuration configuration = bundle.getSqlSessionFactory().getConfiguration();
        assertThat(configuration.getDatabaseId(), is("--test-value--"));
    }
}