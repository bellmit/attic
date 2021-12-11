package com.loginbox.dropwizard.mybatis;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.setup.Bootstrap;

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
public abstract class MybatisBundle<T extends io.dropwizard.Configuration>
        extends AbstractMybatisBundle
        implements ConfiguredBundle<T>, DatabaseConfiguration<T> {
    /**
     * Creates a bundle with no mappers configured automatically. The bundle's Mybatis {@link
     * org.apache.ibatis.session.SqlSessionFactory} can still be configured by overriding {@link
     * #configureMybatis(org.apache.ibatis.session.Configuration)}.
     */
    public MybatisBundle() {
        super();
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
        super(mapper, mappers);
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
        super(packageName, packageNames);
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
        ManagedDataSource dataSource = getManagedDataSource(configuration, environment);
        run(dataSource, environment);
    }

    /**
     * Create (or obtain) the managed datasource used by this bundle. By default, this will create a new datasource from
     * the passed configuration, but you can override this method to provide your own.
     *
     * @param configuration
     *         the configuration to obtain datasource configuration from.
     * @param environment
     *         the environment to use when managing the datasource.
     * @return a managed data source.
     */
    private ManagedDataSource getManagedDataSource(T configuration, io.dropwizard.setup.Environment environment) {
        PooledDataSourceFactory dataSourceFactory = getDataSourceFactory(configuration);
        ManagedDataSource dataSource = dataSourceFactory.build(environment.metrics(), getName());
        environment.lifecycle().manage(dataSource);
        return dataSource;
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
}
