package com.loginbox.app.password;

import com.loginbox.app.LoginBoxConfiguration;
import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PasswordBundle implements ConfiguredBundle<LoginBoxConfiguration> {
    private PasswordValidator passwordValidator = null;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    @Override
    public void run(LoginBoxConfiguration configuration, Environment environment) throws Exception {
        this.passwordValidator = configuration
                .getPasswordValidatorFactory()
                .build();
    }

    public PasswordValidator getPasswordValidator() {
        return passwordValidator;
    }
}
