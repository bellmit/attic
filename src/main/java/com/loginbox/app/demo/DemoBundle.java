package com.loginbox.app.demo;

import com.loginbox.app.demo.resources.GreetingResource;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DemoBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    @Override
    public void run(Environment environment) {
        environment.jersey()
            .register(new GreetingResource());
    }
}
