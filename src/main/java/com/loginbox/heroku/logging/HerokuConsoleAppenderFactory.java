package com.loginbox.heroku.logging;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.ConsoleAppenderFactory;

/**
 * A console appender factory appropriate for Heroku. This writes messages to stdout, with a reduced {@link
 * #HEROKU_LOG_FORMAT log format} more appropriate to Heroku.
 * <p>
 * {@link com.loginbox.heroku.logging.HerokuLoggingFactory} automatically provides a HerokuConsoleAppenderFactory.
 * However, you can also use this appender in your own configuration YAML directly:
 * <pre>
 * logging:
 *     appenders:
 *         - type: heroku
 * </pre>
 */
@JsonTypeName("heroku")
public class HerokuConsoleAppenderFactory extends ConsoleAppenderFactory {
    /**
     * The default log format for Heroku Dropwizard apps. Includes the severity of the message, the logger name, the log
     * message, and any exception traceback, including root causes.
     */
    public static final String HEROKU_LOG_FORMAT = "%-5p %c: %m%n%rEx";

    {
        setLogFormat(HEROKU_LOG_FORMAT);
    }
}
