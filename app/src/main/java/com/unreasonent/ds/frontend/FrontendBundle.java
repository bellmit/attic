package com.unreasonent.ds.frontend;

import com.unreasonent.ds.frontend.resources.Frontend;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FrontendBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        /* No bundles, just resources. */
    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(new Frontend());
    }
}
