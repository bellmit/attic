package com.loginbox.app.dropwizard;

import com.loginbox.app.LoginBoxConfiguration;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BundleTestCase {
    @SuppressWarnings("unchecked")
    protected final Bootstrap<LoginBoxConfiguration> bootstrap = mock(Bootstrap.class);
    protected final Environment environment = mock(Environment.class);
    protected final JerseyEnvironment jersey = mock(JerseyEnvironment.class);

    @Before
    public void wireMocks() {
        when(environment.jersey()).thenReturn(jersey);
    }
}
