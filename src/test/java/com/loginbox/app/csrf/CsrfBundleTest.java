package com.loginbox.app.csrf;

import com.loginbox.app.csrf.context.CsrfCookies;
import com.loginbox.app.csrf.context.CsrfValidator;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsrfBundleTest {
    private final CsrfBundle bundle = new CsrfBundle();

    private final Environment environment = mock(Environment.class);
    private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);

    @Before
    public void wireMocks() {
        when(environment.jersey()).thenReturn(jersey);
    }

    @Test
    public void bindsValidator() {
        bundle.run(environment);

        verify(jersey).register(isA(CsrfValidator.Binder.class));
    }

    @Test
    public void bindsCookies() {
        bundle.run(environment);

        verify(jersey).register(isA(CsrfCookies.Binder.class));
    }

    @Test
    public void bindsSecretGenerator() {
        bundle.run(environment);

        verify(jersey).register(isA(SecretGenerator.Binder.class));
    }
}