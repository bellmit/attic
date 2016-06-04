package com.unreasonent.ds.auth;

import com.unreasonent.ds.BundleTestCase;
import com.unreasonent.ds.auth.resources.WhoAmI;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

public class AuthBundleTest extends BundleTestCase {
    private final AuthBundle bundle = new AuthBundle();

    @Test
    public void jerseyRegistration() throws Exception {
        bundle.run(configuration, environment);

        verify(jersey).register(isA(AuthDynamicFeature.class));
        verify(jersey).register(isA(AuthValueFactoryProvider.Binder.class));
        verify(jersey).register(isA(WhoAmI.class));
    }
}