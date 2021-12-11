package com.loginbox.heroku.logging;

import io.dropwizard.logging.ConsoleAppenderFactory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HerokuConsoleAppenderFactoryTest {
    private final HerokuConsoleAppenderFactory console = new HerokuConsoleAppenderFactory();

    @Test
    public void isHerokuFriendly() {
        // Dumbest possible test, but hey: it's better than nothing.
        assertThat(console.getLogFormat(), is(HerokuConsoleAppenderFactory.HEROKU_LOG_FORMAT));
        assertThat(console.getTarget(), is(ConsoleAppenderFactory.ConsoleStream.STDOUT));
    }
}