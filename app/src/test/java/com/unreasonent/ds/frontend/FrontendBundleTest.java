package com.unreasonent.ds.frontend;

import com.unreasonent.ds.BundleTestCase;
import com.unreasonent.ds.frontend.resources.Frontend;
import io.dropwizard.views.ViewBundle;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

public class FrontendBundleTest extends BundleTestCase {
    @Test
    public void registersResources() {
        FrontendBundle bundle = new FrontendBundle();

        bundle.run(environment);

        verify(jersey).register(isA(Frontend.class));
    }
}