package com.loginbox.app;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LoginBoxTest {
    private final Bootstrap<LoginBoxConfiguration> bootstrap = mock(Bootstrap.class);
    private final LoginBox application = new LoginBox();

    @Test
    public void configuresExpectedBundles() {
        /* This is a dumb test: it more or less reiterates the body of the methods under test. However, it's better than nothing, I hope... */
        application.initialize(bootstrap);

        verify(bootstrap).addBundle(isA(AssetsBundle.class));
    }
}
