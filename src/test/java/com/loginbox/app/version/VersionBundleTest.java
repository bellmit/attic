package com.loginbox.app.version;

import com.loginbox.app.version.filters.VersionFilter;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VersionBundleTest {
    private final VersionBundle bundle = new VersionBundle("com.loginbox.app", "Login Box");
    private Bootstrap<?> bootstrap = mock(Bootstrap.class);
    private Environment environment = mock(Environment.class);
    private JerseyEnvironment jersey = mock(JerseyEnvironment.class);

    @Before
    public void wireMocks() {
        when(environment.jersey()).thenReturn(jersey);
    }

    @Test
    public void registersVersionFilter() {
        bundle.initialize(bootstrap);
        bundle.run(environment);

        verify(jersey).register(isA(VersionFilter.class));
    }
}
