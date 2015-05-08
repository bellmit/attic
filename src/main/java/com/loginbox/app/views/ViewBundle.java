package com.loginbox.app.views;

import com.google.common.collect.ImmutableMap;
import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;

/**
 * Configures Dropwizard Views for login-box. This largely uses the stock configuration, but adds Jersey's declarative
 * link support.
 * <p>
 * Views should extend {@link com.loginbox.app.views.ViewConvention} where possible, to gain some default niceties like
 * a consistent {@code links} section.
 */
public class ViewBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bootstrap.addBundle(new io.dropwizard.views.ViewBundle<Configuration>() {
            @Override
            public ImmutableMap<String, ImmutableMap<String, String>> getViewConfiguration(Configuration configuration) {
                return ImmutableMap.of();
            }
        });
    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(DeclarativeLinkingFeature.class);
    }
}
