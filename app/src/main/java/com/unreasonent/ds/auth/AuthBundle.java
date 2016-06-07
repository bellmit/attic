package com.unreasonent.ds.auth;

import com.unreasonent.ds.DistantShoreConfiguration;
import com.unreasonent.ds.auth.authenticator.JwtAuthenticator;
import com.unreasonent.ds.auth.resources.WhoAmI;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AuthBundle implements ConfiguredBundle<DistantShoreConfiguration> {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        /* No bundles, just Jersey */
    }

    @Override
    public void run(DistantShoreConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new JwtAuthenticator(configuration.getOauth().newVerifier()))
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(new WhoAmI());
    }
}
