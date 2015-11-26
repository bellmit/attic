package com.loginbox.app.setup;

import com.loginbox.app.dropwizard.BundleTestCase;
import com.loginbox.app.setup.filters.RedirectToSetupFilter;
import com.loginbox.app.setup.resources.SetupResource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SetupBundleTest extends BundleTestCase {

    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);
    private final SetupBundle bundle = new SetupBundle() {
        public SqlSessionFactory getSqlSessionFactory() {
            return sqlSessionFactory;
        }
    };

    @Test
    public void run() throws Exception {
        bundle.run(environment);

        verify(jersey).register(isA(SetupResource.class));
        verify(jersey).register(isA(RedirectToSetupFilter.class));
    }
}