package com.loginbox.app.directory;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Provides an instance of {@link Directories}, configured for the current application.
 */
public class DirectoryBundle implements Bundle {
    private Directories directories = new Directories();

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {

    }

    /**
     * @return a {@link com.loginbox.app.directory.Directories} for the current application. This is only guaranteed to
     * be non-{@code null} after calling {@link #run(io.dropwizard.setup.Environment)}.
     */
    public Directories getDirectories() {
        return directories;
    }
}
