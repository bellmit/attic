package com.loginbox.app.views;

import com.loginbox.app.LoginBoxConfiguration;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ViewBundleTest {
    private final Bootstrap<LoginBoxConfiguration> bootstrap = mock(Bootstrap.class);
    private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);
    private final Environment environment = mock(Environment.class);
    private final ViewBundle bundle = new ViewBundle();

    @Before
    public void wireMocks() {
        when(environment.jersey()).thenReturn(jersey);
    }

    @Test
    public void configuresBundles() {
        bundle.initialize(bootstrap);

        verify(bootstrap).addBundle(isA(io.dropwizard.views.ViewBundle.class));
    }

    @Test
    public void configuresJersey() {
        bundle.run(environment);

        verify(jersey).register(DeclarativeLinkingFeature.class);
    }
}