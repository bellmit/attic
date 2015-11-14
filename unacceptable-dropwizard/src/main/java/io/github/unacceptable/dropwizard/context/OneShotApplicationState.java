package io.github.unacceptable.dropwizard.context;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.github.unacceptable.lazy.Lazily;

abstract class OneShotApplicationState<C extends Configuration> implements ApplicationState<C> {
    private DropwizardAppRule<C> rules = null;
    private String appUrl = null;

    @Override
    public String url() {
        return appUrl = Lazily.create(appUrl, this::appUrlFromRule);
    }

    private String appUrlFromRule() {
        DropwizardAppRule<C> appRule = rules();
        int port = appRule.getLocalPort();
        String appUrl = appUrlFromPort(port);
        return appUrl;
    }

    private String appUrlFromPort(int localPort) {
        return String.format("http://localhost:%d/", localPort);
    }

    @Override
    public DropwizardAppRule<C> rules() {
        return rules = Lazily.create(rules, this::newAppRule);
    }

    private DropwizardAppRule<C> newAppRule() {
        Class<? extends Application<C>> mainClass = mainClass();
        String configPath = null;
        ConfigOverride[] overrides = overrides();
        return new DropwizardAppRule<C>(mainClass, configPath, overrides);
    }

    protected abstract ConfigOverride[] overrides();

    protected abstract Class<? extends Application<C>> mainClass();
}
