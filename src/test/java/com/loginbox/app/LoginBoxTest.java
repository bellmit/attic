package com.loginbox.app;

import com.loginbox.app.csrf.CsrfBundle;
import com.loginbox.app.csrf.mybatis.MybatisCsrfBundle;
import com.loginbox.app.csrf.ui.CsrfUiBundle;
import com.loginbox.app.dropwizard.BundleTestCase;
import com.loginbox.app.version.VersionBundle;
import com.loginbox.app.views.ViewBundle;
import com.loginbox.dropwizard.mybatis.MybatisBundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import org.junit.Test;

import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoginBoxTest extends BundleTestCase {
    private final LoginBox application = new LoginBox();

    @Test
    /* following _should_ be for the MybatisBundle expectation. */
    @SuppressWarnings("unchecked")
    public void configuresExpectedBundles() {
        /* This is a dumb test: it more or less reiterates the body of the methods under test. However, it's better than nothing, I hope... */
        application.initialize(bootstrap);

        verify(bootstrap).addBundle(isA(VersionBundle.class));
        verify(bootstrap).addBundle(isA(AssetsBundle.class));
        verify(bootstrap).addBundle(isA(ViewBundle.class));
        verify(bootstrap).addBundle(isA(MigrationsBundle.class));
        verify(bootstrap).addBundle(isA(MybatisBundle.class));
        verify(bootstrap).addBundle(isA(CsrfBundle.class));
        verify(bootstrap).addBundle(isA(MybatisCsrfBundle.class));
        verify(bootstrap).addBundle(isA(CsrfUiBundle.class));
    }
}
