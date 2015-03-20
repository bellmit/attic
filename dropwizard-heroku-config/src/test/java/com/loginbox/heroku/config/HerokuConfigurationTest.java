package com.loginbox.heroku.config;

import com.loginbox.heroku.http.HerokuServerFactory;
import com.loginbox.heroku.logging.HerokuLoggingFactory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class HerokuConfigurationTest {
    private final HerokuConfiguration configuration = new HerokuConfiguration();

    @Test
    public void configuresLogging() {
        assertThat(configuration.getLoggingFactory(), is(instanceOf(HerokuLoggingFactory.class)));
    }

    @Test
    public void configuresServer() {
        assertThat(configuration.getServerFactory(), is(instanceOf(HerokuServerFactory.class)));
    }
}
