package com.loginbox.dropwizard.mybatis;

import com.loginbox.dropwizard.mybatis.healthchecks.SqlSessionFactoryHealthCheck;
import com.loginbox.dropwizard.mybatis.mappers.Ping;
import com.loginbox.dropwizard.mybatis.providers.SqlSessionProvider;
import com.loginbox.dropwizard.mybatis.types.GuavaOptionalTypeHandler;
import com.loginbox.dropwizard.mybatis.types.Java8OptionalTypeHandler;
import com.loginbox.dropwizard.mybatis.types.UriTypeHandler;
import com.loginbox.dropwizard.mybatis.types.UrlTypeHandler;
import com.loginbox.dropwizard.mybatis.types.UuidTypeHandler;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URL;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Provides the MyBatis persistence framework to your Dropwizard app.
 * <p>
 * To use this bundle, first, add a {@link io.dropwizard.db.DataSourceFactory} to your configuration class:
 * <pre>
 * public class HelloWorldConfiguration extends Configuration {
 *    {@literal @}Valid
 *    {@literal @}NotNull
 *     private DataSourceFactory dataSourceFactory = new DataSourceFactory();
 *
 *    {@literal @}JsonProperty("database")
 *     public DataSourceFactory getDataSourceFactory() {
 *         return this.dataSourceFactory;
 *     }
 * }
 * </pre>
 * Then, register an instance of this bundle in your app's {@link io.dropwizard.Application#initialize(io.dropwizard.setup.Bootstrap)}
 * method:
 * <pre>
 * public class HelloWorld extends Application&lt;HelloWorldConfiguration&gt; {
 *
 *     public static void main(String... args) throws Exception {
 *         new HelloWorld().run(args);
 *     }
 *
 *     private final MybatisBundle&lt;HelloWorldConfiguration&gt; mybatisBundle
 *             = new MybatisBundle&lt;HelloWorldConfiguration&gt;("com.example.helloworld") {
 *        {@literal @}Override
 *         public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
 *             return configuration.getDataSourceFactory();
 *         }
 *     };
 *
 *    {@literal @}Override
 *     public void initialize(Bootstrap&lt;HelloWorldConfiguration&gt; bootstrap) {
 *         bootstrap.addBundle(mybatisBundle);
 *     }
 *     // . . .
 * }
 * </pre>
 * This will automatically scan the named package(s) for MyBatis mapper {@code .xml} files and interfaces. It will also
 * register a {@link com.loginbox.dropwizard.mybatis.healthchecks.SqlSessionFactoryHealthCheck health check} for your
 * database pool, which you can monitor via the health checks admin endpoint.
 * <p>
 * To use MyBatis in your app, once configured, call {@link #getSqlSessionFactory()} in your own app's {@link
 * io.dropwizard.Application#run(io.dropwizard.Configuration, io.dropwizard.setup.Environment)} method:
 * <pre>
 *    {@literal @}Override
 *     public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {
 *         SqlSessionFactory sessionFactory = mybatisBundle.getSqlSessionFactory();
 *         environment.jersey().register(new ExampleResource(sessionFactory));
 *         // . . .
 *     }
 * </pre>
 * <p>
 * There are three ways to configure MyBatis, depending on your needs: <ul> <li>The {@link #MybatisBundle(Class,
 * Class...)} constructor accepts an explicit list of mapper interfaces to configure. The corresponding SQL can be
 * stored in annotations on the mapper interfaces themselves, or in correspondingly-named {@code .xml} files in the same
 * package. For example, {@code com.example.mappers.Users} would correspond with the file {@code
 * com/example/mappers/Users.xml}.</li> <li>The {@link #MybatisBundle(String, String...)} constructor accepts a list of
 * packages to scan. Any mapper interfaces defined in these packages <em>or subpackages of these packages</em> will be
 * detected and configured. As with the explicit case, SQL for mappers can be stored in the mapper interface itself, or
 * in XML files.</li> <li>In either of the above cases, or using the {@link #MybatisBundle()} constructor, you can
 * optionally override the {@link #configureMybatis(org.apache.ibatis.session.Configuration)} method to customize the
 * Mybatis configuration yourself.</li> </ul>
 *
 * @param <T>
 *         Your application's configuration class.
 */
public abstract class MybatisBundle<T extends io.dropwizard.Configuration> implements ConfiguredBundle<T>, DatabaseConfiguration<T> {

    private SqlSessionFactory sqlSessionFactory = null;

    private final Consumer<Configuration> configureCallback;

    /**
     * Creates a bundle with no mappers configured automatically. The bundle's Mybatis {@link
     * org.apache.ibatis.session.SqlSessionFactory} can still be configured by overriding {@link
     * #configureMybatis(org.apache.ibatis.session.Configuration)}.
     */
    public MybatisBundle() {
        this.configureCallback = config -> {
        };
    }

    /**
     * Creates a bundle with an explicit list of mapper interfaces. The bundle's Mybatis {@link
     * org.apache.ibatis.session.SqlSessionFactory} can be further configured by overriding {@link
     * #configureMybatis(org.apache.ibatis.session.Configuration)}.
     *
     * @param mapper
     *         the first mapper to register.
     * @param mappers
     *         the remaining mappers to register.
     */
    // The wonky signature avoids ambiguity with the () and (String...) cases.
    public MybatisBundle(Class<?> mapper, Class<?>... mappers) {
        this.configureCallback = configure(Configuration::addMapper, mapper, mappers);
    }

    /**
     * Creates a bundle by scanning packages for mappers to configure. Mybatis will automatically scan subpackages of
     * the named packages. The bundle's Mybatis {@link org.apache.ibatis.session.SqlSessionFactory} can be further
     * configured by overriding {@link #configureMybatis(org.apache.ibatis.session.Configuration)}.
     *
     * @param packageName
     *         the first package to scan.
     * @param packageNames
     *         the remaining packages to scan.
     */
    // The wonky signature avoids ambiguity with the () and (Class...) cases.
    public MybatisBundle(String packageName, String... packageNames) {
        this.configureCallback = configure(Configuration::addMappers, packageName, packageNames);
    }

    /**
     * Returns the {@link org.apache.ibatis.session.SqlSessionFactory} created by this bundle. Until {@link
     * #run(io.dropwizard.Configuration, io.dropwizard.setup.Environment)} completes, this will return
     * <code>null</code>.
     * <p>
     * Care is needed when using this bundle in other bundles: this method will return null throughout the
     * <code>initialize</code> phase of Dropwizard's startup. It's generally better to pass this whole bundle to its
     * dependents, and let them obtain session factories at the appropriate time, than it is to take the session factory
     * from this bundle and pass it to other bundles. This awkwardness is inherent in MyBatis' design.
     *
     * @return the configured session factory for this bundle, or <code>null</code> if the bundle hasn't been started
     * yet.
     */
    @Nullable
    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    /**
     * Does nothing on its own, but may be overridden to customize Mybatis configuration more flexibly.
     * <p>
     * This will be called <em>after</em> <ol> <li>the bundle's own suite of type mappers have been registered in the
     * configuration</li> <li>the bundle's own mappers and other objects have been registered in the configuration,
     * and</li> <li>any mappers or packages from the constructor have been registered in the configuration.</li> </ol>
     *
     * @param configuration
     *         the MyBatis configuration in flight.
     * @throws Exception
     *         if the custom configuration fails. This will be thrown out through {@link
     *         #run(io.dropwizard.Configuration, io.dropwizard.setup.Environment)}, which will normally abort
     *         application startup.
     */
    protected void configureMybatis(Configuration configuration) throws Exception {
    }

    /**
     * Creates the bundle's MyBatis session factory and registers health checks.
     *
     * @param configuration
     *         the application's configuration.
     * @param environment
     *         the Dropwizard environment being started.
     * @throws Exception
     *         if MyBatis setup fails for any reason. MyBatis exceptions will be thrown as-is.
     */
    @Override
    public void run(T configuration, io.dropwizard.setup.Environment environment) throws Exception {
        ManagedDataSource dataSource = buildDataSource(configuration, environment);
        environment.lifecycle().manage(dataSource);

        sqlSessionFactory = createSqlSessionFactory(dataSource);

        environment.healthChecks().register(getName(), new SqlSessionFactoryHealthCheck(sqlSessionFactory));
        environment.jersey().register(SqlSessionProvider.binder(sqlSessionFactory));
    }

    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        Configuration mybatisConfiguration = buildMybatisConfiguration(dataSource);
        return new SqlSessionFactoryBuilder().build(mybatisConfiguration);
    }

    private Configuration buildMybatisConfiguration(DataSource dataSource) throws Exception {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment mybatisEnvironment = new Environment(getName(), transactionFactory, dataSource);
        Configuration configuration = new Configuration(mybatisEnvironment);
        registerTypeHandlers(configuration.getTypeHandlerRegistry());
        registerOwnMappers(configuration);
        registerClientMappers(configuration);
        configureMybatis(configuration);
        return configuration;
    }

    protected void registerTypeHandlers(TypeHandlerRegistry registry) {
        registry.register(UUID.class, new UuidTypeHandler());
        registry.register(java.util.Optional.class, new Java8OptionalTypeHandler());
        registry.register(com.google.common.base.Optional.class, new GuavaOptionalTypeHandler());
        registry.register(URL.class, new UrlTypeHandler());
        registry.register(URI.class, new UriTypeHandler());
    }

    private void registerClientMappers(Configuration configuration) {
        configureCallback.accept(configuration);
    }

    private void registerOwnMappers(Configuration configuration) {
        configuration.addMapper(Ping.class);
    }

    private ManagedDataSource buildDataSource(T configuration, io.dropwizard.setup.Environment environment) {
        PooledDataSourceFactory dataSourceFactory = getDataSourceFactory(configuration);
        return dataSourceFactory.build(environment.metrics(), getName());
    }

    /* @SafeVarargs would be appropriate, but it's not permitted on private methods until Java 9. */
    @SuppressWarnings("unchecked")
    private <V> Consumer<Configuration> configure(BiConsumer<Configuration, V> valueApplicator, V value, V... values) {
        return (configuration) -> {
            valueApplicator.accept(configuration, value);
            for (V v : values) {
                valueApplicator.accept(configuration, v);
            }
        };
    }

    /**
     * Initializes the bundle by doing nothing.
     *
     * @param bootstrap
     *         the Dropwizard bootstrap configuration.
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    protected abstract String getName();
}
