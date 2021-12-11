package com.loginbox.heroku.logging;

import com.google.common.collect.ImmutableList;
import io.dropwizard.logging.AppenderFactory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class HerokuLoggingFactoryTest {
    private final HerokuLoggingFactory factory = new HerokuLoggingFactory();

    @Test
    public void defaultHerokuConsoleAppenders() {
        ImmutableList<AppenderFactory> appenders = factory.getAppenders();
        assertThat(appenders.size(), is(1));
        assertThat(appenders.get(0), is(instanceOf(HerokuConsoleAppenderFactory.class)));
    }
}