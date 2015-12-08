package io.github.unacceptable.dropwizard.system;

import io.github.unacceptable.lazy.Lazily;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public abstract class AbstractSystemDriver {

    private String baseUrl = null;

    public String baseUrl() {
        return baseUrl = Lazily.create(baseUrl, this::detectBaseUrl);
    }

    private String detectBaseUrl() {
        if (isAppExternal())  {
            return externalAppBaseUrl();
        } else {
            return internalAppBaseUrl();
        }
    }

    protected abstract String internalAppBaseUrl();

    private String externalAppBaseUrl() {
        return System.getProperty("app.url");
    }

    public boolean isAppExternal() {
        return externalAppBaseUrl() != null;
    }

    protected TestRule alwaysRunRules() {
        return new NoOpRule();
    }

    protected abstract TestRule internalAppRules();

    @Rule
    public TestRule rules() {
        if (isAppExternal()) {
            return alwaysRunRules();
        } else {
            // Run the internal app rules first (eg. setup the database, start the app)
            return RuleChain
                    .outerRule(alwaysRunRules())
                    .around(internalAppRules());
        }
    }

    private class NoOpRule extends ExternalResource {
    }
}
