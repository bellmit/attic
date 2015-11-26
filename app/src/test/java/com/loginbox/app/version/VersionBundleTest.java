package com.loginbox.app.version;

import com.loginbox.app.dropwizard.BundleTestCase;
import com.loginbox.app.version.filters.VersionFilter;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VersionBundleTest extends BundleTestCase {
    private final VersionBundle bundle = new VersionBundle("com.loginbox.app", "Login Box");

    @Test
    public void registersVersionFilter() {
        bundle.initialize(bootstrap);
        bundle.run(environment);

        verify(jersey).register(isA(VersionFilter.class));
    }
}
