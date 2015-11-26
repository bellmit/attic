package com.loginbox.app.csrf.mybatis;

import com.loginbox.app.csrf.mybatis.providers.CsrfRepositoryProvider;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MybatisCsrfBundleTest {
    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);

    private final MybatisCsrfBundle bundle = new MybatisCsrfBundle() {
        @Override
        public SqlSessionFactory getSqlSessionFactory() {
            return sqlSessionFactory;
        }
    };

    private final Environment environment = mock(Environment.class);
    private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);

    @Before
    public void wireMocks() {
        when(environment.jersey()).thenReturn(jersey);
    }

    @Test
    public void installsCsrfRepositoryProvider() {
        bundle.run(environment);

        verify(jersey).register(isA(CsrfRepositoryProvider.Binder.class));
    }
}