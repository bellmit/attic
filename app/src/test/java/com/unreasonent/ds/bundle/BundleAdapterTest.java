package com.unreasonent.ds.bundle;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.Test;

import static com.unreasonent.ds.bundle.WrapsBundle.wrapsBundle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BundleAdapterTest {
    private final Bundle bundle = mock(Bundle.class);

    @Test
    public void wrapperHasBundle() {
        assertThat(BundleAdapter.wrap(bundle), wrapsBundle(equalTo(bundle)));
    }

    @Test
    public void delegatesInitialize() {
        Bootstrap<?> bootstrap = mock(Bootstrap.class);
        BundleAdapter.wrap(bundle).initialize(bootstrap);

        verify(bundle).initialize(bootstrap);
    }

    @Test
    public void delegatesRun() throws Exception {
        Object configuration = new Object();
        Environment environment = mock(Environment.class);

        BundleAdapter.wrap(bundle).run(configuration, environment);
        verify(bundle).run(environment);
    }
}
