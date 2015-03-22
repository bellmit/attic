package com.loginbox.app;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LoginBox extends Application<LoginBoxConfiguration> {
    public static void main(String... args) throws Exception {
        new LoginBox().run(args);
    }

    @Override
    public void initialize(Bootstrap<LoginBoxConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle());
    }

    @Override
    public void run(LoginBoxConfiguration configuration, Environment environment) throws Exception {
    }
}
