package io.github.unacceptable.dropwizard.context;

import io.dropwizard.Configuration;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;

class ExternalApplicationState<C extends Configuration> implements ApplicationState<C> {
    private final String appUrl;

    public ExternalApplicationState(String appUrl) {
        this.appUrl = appUrl;
    }

    @Override
    public String url() {
        return appUrl;
    }

    @Override
    public TestRule rules() {
        return new NoOpRule();
    }

    private class NoOpRule extends ExternalResource {
    }
}
