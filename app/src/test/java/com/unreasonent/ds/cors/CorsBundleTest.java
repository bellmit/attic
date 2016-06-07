package com.unreasonent.ds.cors;

import com.unreasonent.ds.BundleTestCase;
import com.unreasonent.ds.cors.config.CrossOriginFilterFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CorsBundleTest extends BundleTestCase {
    private final CorsBundle bundle = new CorsBundle();

    private final CrossOriginFilterFactory cors = mock(CrossOriginFilterFactory.class);

    @Before
    public void wireCorsConfig() {
        configuration.setCors(cors);
    }

    @Test
    public void registersFilters() throws Exception {
        bundle.run(configuration, environment);

        verify(cors).registerFilter(servlets);
    }
}