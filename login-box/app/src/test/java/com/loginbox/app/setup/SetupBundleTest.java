package com.loginbox.app.setup;

import com.loginbox.app.directory.Directories;
import com.loginbox.app.dropwizard.BundleTestCase;
import com.loginbox.app.password.PasswordValidator;
import com.loginbox.app.setup.filters.RedirectToSetupFilter;
import com.loginbox.app.setup.resources.SetupResource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SetupBundleTest extends BundleTestCase {

    private final Directories directories = mock(Directories.class);
    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);
    private final PasswordValidator passwordValidator = mock(PasswordValidator.class);

    private final SetupBundle bundle = new SetupBundle() {
        @Override
        public PasswordValidator getPasswordValidator() {
            return passwordValidator;
        }

        @Override
        public Directories getDirectories() {
            return directories;
        }

        @Override
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