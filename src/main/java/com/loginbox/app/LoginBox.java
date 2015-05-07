package com.loginbox.app;

import com.loginbox.app.csrf.CsrfBundle;
import com.loginbox.app.csrf.mybatis.MybatisCsrfBundle;
import com.loginbox.app.csrf.ui.CsrfUiBundle;
import com.loginbox.app.version.VersionBundle;
import com.loginbox.app.views.ViewBundle;
import com.loginbox.dropwizard.mybatis.MybatisBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.ibatis.session.SqlSessionFactory;

public class LoginBox extends Application<LoginBoxConfiguration> {

    public static void main(String... args) throws Exception {
        new LoginBox().run(args);
    }

    private final VersionBundle versionBundle = new VersionBundle("com.loginbox.app", "Login Box");
    private final AssetsBundle assetsBundle
            = new AssetsBundle();
    private final ViewBundle viewBundle
            = new ViewBundle();
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
    private final CsrfBundle csrfBundle = new CsrfBundle();
    private final MybatisCsrfBundle mybatisCsrfBundle = new MybatisCsrfBundle() {
        @Override
        public SqlSessionFactory getSqlSessionFactory() {
            return mybatisBundle.getSqlSessionFactory();
        }
    };
    private final CsrfUiBundle csrfUiBundle = new CsrfUiBundle();

    @Override
    public void initialize(Bootstrap<LoginBoxConfiguration> bootstrap) {
        bootstrap.addBundle(versionBundle);
        bootstrap.addBundle(assetsBundle);
        bootstrap.addBundle(viewBundle);
        bootstrap.addBundle(migrationsBundle);
        bootstrap.addBundle(mybatisBundle);
        bootstrap.addBundle(csrfBundle);
        bootstrap.addBundle(mybatisCsrfBundle);
        bootstrap.addBundle(csrfUiBundle);
    }

    @Override
    public void run(LoginBoxConfiguration configuration, Environment environment) throws Exception {
    }
}
