package com.unreasonent.ds;

import com.unreasonent.ds.auth.AuthBundle;
import com.unreasonent.ds.cors.CorsBundle;
import com.unreasonent.ds.database.DatabaseBundle;
import io.dropwizard.setup.Bootstrap;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DistantShoreTest {
    @SuppressWarnings("unchecked")
    private final Bootstrap<DistantShoreConfiguration> bootstrap = mock(Bootstrap.class);

    private final DistantShore app = new DistantShore();

    @Test
    public void registersBundles() {
        app.initialize(bootstrap);

        verify(bootstrap).addBundle(isA(CorsBundle.class));
        verify(bootstrap).addBundle(isA(AuthBundle.class));
        verify(bootstrap).addBundle(isA(DatabaseBundle.class));
    }
}