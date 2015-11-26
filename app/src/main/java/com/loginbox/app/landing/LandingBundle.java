package com.loginbox.app.landing;

import com.loginbox.app.landing.resources.LandingResource;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Registers Login Box's landing page and config reporter.
 */
public class LandingBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    /**
     * Registers the following resources: <ul> <li>{@link com.loginbox.app.landing.resources.LandingResource}</li>
     * </ul>
     */
    @Override
    public void run(Environment environment) {
        LandingResource landingResource = new LandingResource();

        environment.jersey().register(landingResource);
    }
}
