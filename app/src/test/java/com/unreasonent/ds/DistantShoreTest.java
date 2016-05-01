package com.unreasonent.ds;

import com.unreasonent.ds.frontend.FrontendBundle;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.views.ViewBundle;
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

        verify(bootstrap).addBundle(isViewBundle());
        verify(bootstrap).addBundle(isA(FrontendBundle.class));
    }

    @SuppressWarnings("unchecked")
    private <T extends Configuration> ViewBundle<T> isViewBundle() {
        return isA(ViewBundle.class);
    }
}