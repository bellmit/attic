package com.loginbox.dropwizard.mybatis;

import io.dropwizard.Bundle;
import io.dropwizard.db.ManagedDataSource;
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
 * Then, register an instance of this bundle in your app's {@link io.dropwizard.Application#initialize(Bootstrap)}
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
 * There are three ways to configure MyBatis, depending on your needs: <ul> <li>The {@link
 * #DatasourceMybatisBundle(Class, Class...)} constructor accepts an explicit list of mapper interfaces to configure.
 * The corresponding SQL can be stored in annotations on the mapper interfaces themselves, or in correspondingly-named
 * {@code .xml} files in the same package. For example, {@code com.example.mappers.Users} would correspond with the file
 * {@code com/example/mappers/Users.xml}.</li> <li>The {@link #DatasourceMybatisBundle(String, String...)} constructor
 * accepts a list of packages to scan. Any mapper interfaces defined in these packages <em>or subpackages of these
 * packages</em> will be detected and configured. As with the explicit case, SQL for mappers can be stored in the mapper
 * interface itself, or in XML files.</li> <li>In either of the above cases, or using the {@link
 * #DatasourceMybatisBundle()} constructor, you can optionally override the {@link
 * #configureMybatis(org.apache.ibatis.session.Configuration)} method to customize the Mybatis configuration
 * yourself.</li> </ul>
 */
public abstract class DatasourceMybatisBundle
        extends AbstractMybatisBundle
        implements Bundle {
    /**
     * Creates a bundle with no mappers configured automatically. The bundle's Mybatis {@link
     * org.apache.ibatis.session.SqlSessionFactory} can still be configured by overriding {@link
     * #configureMybatis(org.apache.ibatis.session.Configuration)}.
     */
    public DatasourceMybatisBundle() {
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
    public DatasourceMybatisBundle(Class<?> mapper, Class<?>... mappers) {
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
    public DatasourceMybatisBundle(String packageName, String... packageNames) {
        super(packageName, packageNames);
    }

    /**
     * Creates the bundle's MyBatis session factory and registers health checks.
     *
     * @param environment
     *         the Dropwizard environment being started.
     * @throws RuntimeException
     *         if MyBatis setup fails for any reason. MyBatis exceptions will be thrown wrapped in a RuntimeException.
     */
    @Override
    public void run(io.dropwizard.setup.Environment environment) {
        ManagedDataSource dataSource = getManagedDataSource();
        try {
            run(dataSource, environment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create (or obtain) the managed datasource used by this bundle.
     *
     * @return a managed data source.
     */
    protected abstract ManagedDataSource getManagedDataSource();

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
