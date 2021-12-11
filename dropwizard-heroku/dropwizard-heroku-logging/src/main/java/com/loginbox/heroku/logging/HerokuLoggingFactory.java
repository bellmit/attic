package com.loginbox.heroku.logging;

import com.google.common.collect.Lists;
import io.dropwizard.logging.AppenderFactory;
import io.dropwizard.logging.ConsoleAppenderFactory;
import io.dropwizard.logging.DefaultLoggingFactory;
import io.dropwizard.logging.LoggingFactory;

import java.util.List;

/**
 * A logging factory appropriate for Heroku. On Heroku, logging is normally delivered to stdout or stderr, and there is
 * no syslog. Log files don't work, either, since files can only be written to the current dyno, and dyno filesystems
 * are silently destroyed whenever a server exits or restarts. Heroku also provides some basic logging infrastructure,
 * such as timestamps.
 *
 * @see HerokuConsoleAppenderFactory
 */
public class HerokuLoggingFactory extends DefaultLoggingFactory {
    /**
     * Creates the default appenders for a Heroku application.
     *
     * @return a single-element list containing a {@link #consoleAppender() console appender}.
     */
    public static List<AppenderFactory> defaultAppenders() {
        return Lists.<AppenderFactory>newArrayList(
                consoleAppender()
        );

    }

    /**
     * Creates a console appender factory configured for Heroku.
     *
     * @return a {@link HerokuConsoleAppenderFactory}.
     */
    private static ConsoleAppenderFactory consoleAppender() {
        return new HerokuConsoleAppenderFactory();
    }

    {
        setAppenders(defaultAppenders());
    }
}
