package io.github.unacceptable.dropwizard.context;

import io.dropwizard.Configuration;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.github.unacceptable.lazy.Lazily;

public class OneShotApplicationState<C extends Configuration> implements ApplicationState<C> {
    private DropwizardAppRule<C> rules = null;
    private String appUrl = null;
    private final DropwizardTestSupport<C> testSupport;

    public OneShotApplicationState(final DropwizardTestSupport<C> testSupport) {
        this.testSupport = testSupport;
    }

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
        return new DropwizardAppRule<C>(testSupport);
    }

}
