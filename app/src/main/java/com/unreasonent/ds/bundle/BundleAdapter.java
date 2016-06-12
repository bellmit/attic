package com.unreasonent.ds.bundle;

import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BundleAdapter<T> implements ConfiguredBundle<T> {
    public static <T> ConfiguredBundle<T> wrap(Bundle bundle) {
        return new BundleAdapter<>(bundle);
    }

    public static <T> ConfiguredBundle<T> wrap(ConfiguredBundle<T> bundle) {
        return bundle;
    }

    final Bundle bundle;

    private BundleAdapter(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        bundle.run(environment);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bundle.initialize(bootstrap);
    }
}
