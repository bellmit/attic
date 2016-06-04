package com.unreasonent.ds;

import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class DistantShoreTest {
    @SuppressWarnings("unchecked")
    private final Bootstrap<DistantShoreConfiguration> bootstrap = mock(Bootstrap.class);

    private final DistantShore app = new DistantShore();

    @Test
    public void registersBundles() {
        app.initialize(bootstrap);

        verify(bootstrap, never()).addBundle(isBundle());
        verify(bootstrap, never()).addBundle(isConfiguredBundle());
    }

    @SuppressWarnings("unchecked")
    private <T extends Configuration> ConfiguredBundle<T> isConfiguredBundle() {
        return isA(ConfiguredBundle.class);
    }

    @SuppressWarnings("unchecked")
    private Bundle isBundle() {
        return isA(Bundle.class);
    }
}