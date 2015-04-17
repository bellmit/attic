package com.loginbox.app;

import com.loginbox.app.version.VersionBundle;
import com.loginbox.dropwizard.mybatis.MybatisBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LoginBox extends Application<LoginBoxConfiguration> {

    public static void main(String... args) throws Exception {
        new LoginBox().run(args);
    }

    private final VersionBundle versionBundle = new VersionBundle("com.loginbox.app", "Login Box");
    private final AssetsBundle assetsBundle
            = new AssetsBundle();
    private final MigrationsBundle<LoginBoxConfiguration> migrationsBundle
            = new MigrationsBundle<LoginBoxConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(LoginBoxConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };
    private final MybatisBundle<LoginBoxConfiguration> mybatisBundle
            = new MybatisBundle<LoginBoxConfiguration>("com.loginbox.app") {
        @Override
        public DataSourceFactory getDataSourceFactory(LoginBoxConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<LoginBoxConfiguration> bootstrap) {
        bootstrap.addBundle(versionBundle);
        bootstrap.addBundle(assetsBundle);
        bootstrap.addBundle(migrationsBundle);
        bootstrap.addBundle(mybatisBundle);
    }

    @Override
    public void run(LoginBoxConfiguration configuration, Environment environment) throws Exception {
    }
}
