package com.unreasonent.ds;

import com.loginbox.heroku.config.HerokuConfiguration;
import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import org.junit.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class DistantShoreTest {
    @SuppressWarnings("unsafe")
    private final Bootstrap<DistantShoreConfiguration> bootstrap = mock(Bootstrap.class);

    private final DistantShore app = new DistantShore();

    @Test
    public void registersBundles() {
        app.initialize(bootstrap);

        verify(bootstrap, never()).addBundle(any(Bundle.class));
        verify(bootstrap, never()).addBundle(any(ConfiguredBundle.class));
    }
}