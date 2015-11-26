package com.loginbox.app.csrf.mybatis;

import com.loginbox.app.csrf.CsrfBundle;
import com.loginbox.app.csrf.mybatis.providers.CsrfRepositoryProvider;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Wires up extra dependencies to allow {@link com.loginbox.app.csrf.CsrfBundle} to store tokens in Mybatis. The
 * CsrfBundle must be registered separately, either before or after this bundle; this arrangement allows callers to
 * decide whether to use CsrfBundle itself, or a subclass.
 */
public abstract class MybatisCsrfBundle implements Bundle {
    private final CsrfBundle csrfBundle = new CsrfBundle();

    /**
     * Does nothing.
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    /**
     * Configures additional dependency injectors for Mybatis support.
     *
     * @param environment
     *         the Dropwizard environment to configure.
     */
    @Override
    public void run(Environment environment) {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        environment.jersey().register(CsrfRepositoryProvider.binder(this::getSqlSessionFactory));
    }

    public abstract SqlSessionFactory getSqlSessionFactory();
}
