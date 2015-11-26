package com.loginbox.app.views;

import com.loginbox.app.LoginBoxConfiguration;
import com.loginbox.app.dropwizard.BundleTestCase;
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

public class ViewBundleTest extends BundleTestCase {
    private final ViewBundle bundle = new ViewBundle();

    @Test
    /* This _should_ be catching the warning from the ViewBundle expectation. */
    @SuppressWarnings("unchecked")
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