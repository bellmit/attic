package com.loginbox.app.landing;

import com.loginbox.app.dropwizard.BundleTestCase;
import com.loginbox.app.landing.resources.LandingResource;
import org.junit.Test;

import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.verify;

public class LandingBundleTest extends BundleTestCase {
    private final LandingBundle bundle = new LandingBundle();

    @Test
    public void registersResources() {
        bundle.run(environment);

        verify(jersey).register(isA(LandingResource.class));
    }
}