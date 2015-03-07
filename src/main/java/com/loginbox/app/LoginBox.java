package com.loginbox.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class LoginBox extends Application<LoginBoxConfiguration> {
    public static void main(String... args) throws Exception {
        new LoginBox().run(args);
    }

    @Override
    public void run(LoginBoxConfiguration configuration, Environment environment) throws Exception {

    }
}
