package com.loginbox.app.csrf.ui;

import com.loginbox.app.csrf.ui.exceptions.InvalidCsrfTokenViewTranslator;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CsrfUiBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(new InvalidCsrfTokenViewTranslator());
    }
}
