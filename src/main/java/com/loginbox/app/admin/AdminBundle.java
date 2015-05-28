package com.loginbox.app.admin;

import com.loginbox.app.admin.resources.AdminResource;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Registers the administrative UI and API. This bundle allows logged-in users with the appropriate permissions to
 * reconfigure Login Box while it's operating.
 */
public class AdminBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    /**
     * Registers resources:
     * <ul>
     *     <li>{@link com.loginbox.app.admin.resources.AdminResource}</li>
     * </ul>
     */
    @Override
    public void run(Environment environment) {
        environment.jersey().register(new AdminResource());
    }
}
