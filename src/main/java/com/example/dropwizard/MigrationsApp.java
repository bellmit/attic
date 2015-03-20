package com.example.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MigrationsApp extends Application<MigrationsAppConfiguration> {
    public static void main(String... args) throws Exception {
        new MigrationsApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<MigrationsAppConfiguration> bootstrap) {
        bootstrap.addCommand(new DemoCommand());
    }

    @Override
    public void run(MigrationsAppConfiguration configuration, Environment environment) throws Exception {

    }
}
