package io.github.unacceptable.dropwizard.context;

import com.google.common.collect.ObjectArrays;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.testing.ConfigOverride;
import io.github.unacceptable.lazy.Lazily;
import org.junit.rules.TestRule;

import static io.github.unacceptable.lazy.Lazily.systemProperty;

/**
 * Starts a Dropwizard application around each test run, unless a previously-running application's URL has been
 * provided.
 * <p>
 * To target tests to an existing app, set the {@code app.url} system property. If not set, this context will boot an
 * instance of {@link #mainClass() your application} for the duration of the test, then shut it down.
 * <p>
 * When booting an application, the {@link #overrides() configuration overrides} will be applied. By default, the only
 * override applied sets the logging level to {@link #logLevel()}, which will suppress the vast sea of bootup and
 * shutdown diagnostic messages. However, additional overrides can be supplied via the {@link #extraOverrides()} method,
 * or the complete overrides list can be customized by overriding {@link #overrides()} directly.
 */
public abstract class ApplicationContext<C extends Configuration> {
    private ApplicationState<C> applicationState = null;
    private String logLevel = null;

    private ApplicationState<C> applicationState() {
        return applicationState = Lazily.create(applicationState, this::detectApplicationState);
    }

    private ApplicationState<C> detectApplicationState() {
        String appUrl = System.getProperty("app.url");
        if (appUrl == null) {
            return new OneShotApplicationState<C>() {
                @Override
                protected ConfigOverride[] overrides() {
                    return ApplicationContext.this.overrides();
                }

                @Override
                protected Class<? extends Application<C>> mainClass() {
                    return ApplicationContext.this.mainClass();
                }
            };
        }
        return new ExternalApplicationState<>(appUrl);
    }

    /**
     * @return the application's base URL.
     */
    public String url() {
        return applicationState()
                .url();
    }

    /**
     * @param path
     *         a path relative to the application root.
     * @return the absolute URL of the same path within the application.
     */
    public String url(String path) {
        String baseUrl = Strings.stripTrailingSlashes(url());
        path = Strings.stripLeadingSlashes(path);

        return String.format("%s/%s", baseUrl, path);
    }

    /**
     * @return the log threshold of the app booted by this context, or the {@code app.log.level} system property.
     */
    public String logLevel() {
        return logLevel = systemProperty(logLevel, "app.log.level", this::defaultLogThreshold);
    }

    /**
     * @return the default log threshold to use if the {@code app.log.level} property is not set.
     */
    protected String defaultLogThreshold() {
        return "WARN";
    }

    /**
     * @return if the app must be booted by this context, then this rule will ensure that the app is booted and torn
     * down.
     */
    public TestRule rules() {
        return applicationState()
                .rules();
    }

    protected ConfigOverride[] overrides() {
        ConfigOverride[] defaultOverrides = defaultOverrides();
        ConfigOverride[] extraOverrides = extraOverrides();

        return ObjectArrays.concat(defaultOverrides, extraOverrides, ConfigOverride.class);
    }

    private ConfigOverride[] defaultOverrides() {
        return new ConfigOverride[]{
                ConfigOverride.config("logging.level", logLevel()),
        };
    }

    protected ConfigOverride[] extraOverrides() {
        return new ConfigOverride[]{};
    }

    protected abstract Class<? extends Application<C>> mainClass();
}
