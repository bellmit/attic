package io.github.unacceptable.dropwizard.context;

import io.dropwizard.Configuration;
import org.junit.rules.TestRule;

interface ApplicationState<C extends Configuration> {
    public String url();

    public TestRule rules();
}
