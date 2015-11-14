package io.github.unacceptable.dropwizard.context;

import com.google.common.base.Optional;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.github.unacceptable.lazy.Lazily;
import org.junit.rules.TestRule;

import javax.annotation.Nullable;

import java.util.Arrays;

import static io.github.unacceptable.lazy.Lazily.systemProperty;

/**
 * Starts a Dropwizard application around each test run, unless a previously-running application's URL has been
 * provided.
 * <p>
 * To target tests to an existing app, set the {@code app.url} system property. If not set, this context will boot an
 * instance of your application for the duration of the test, then shut it down.
 * <p>
 * When booting an application, the configuration overrides will be applied. By default, the only
 * override applied sets the logging level to {@link #logLevel()}, which will suppress the vast sea of bootup and
 * shutdown diagnostic messages.
 */
public class ApplicationContext<C extends Configuration> {

    private final Class<? extends Application<C>> applicationClass;
    private final String configPath;
    private final Optional<String> customPropertyPrefix;
    private final ConfigOverride[] configOverrides;
    private ApplicationState<C> applicationState = null;
    private String logLevel = null;

    public ApplicationContext(Class<? extends Application<C>> applicationClass) {
        this(applicationClass, (String) null);
    }

    public ApplicationContext(Class<? extends Application<C>> applicationClass,
                              @Nullable String configPath,
                              ConfigOverride... configOverrides) {
        this(applicationClass, configPath, Optional.<String>absent(), configOverrides);
    }

    public ApplicationContext(Class<? extends Application<C>> applicationClass, String configPath,
                              Optional<String> customPropertyPrefix, ConfigOverride... configOverrides) {
        this.applicationClass = applicationClass;
        this.configPath = configPath;
        this.customPropertyPrefix = customPropertyPrefix;
        this.configOverrides = configOverrides;
    }

    private ApplicationState<C> applicationState() {
        return applicationState = Lazily.create(applicationState, this::detectApplicationState);
    }

    private ApplicationState<C> detectApplicationState() {
        String appUrl = System.getProperty("app.url");
        if (appUrl == null) {
            return new OneShotApplicationState<>(dropwizardTestSupport());
        }
        return new ExternalApplicationState<>(appUrl);
    }

    protected DropwizardTestSupport<C> dropwizardTestSupport() {
        return new DropwizardTestSupport<C>(applicationClass, configPath, customPropertyPrefix,
                                     appendLogLevelOverride(configOverrides));
    }

    /**
     * @return the application's base URL.
     */
    public String url() {
        return applicationState()
                .url();
    }

    /**
     * @param path a path relative to the application root.
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

    private ConfigOverride[] appendLogLevelOverride(final ConfigOverride[] configOverrides) {
        final ConfigOverride[] merged = Arrays.copyOf(configOverrides, configOverrides.length+1);
        merged[merged.length-1] = ConfigOverride.config("logging.level", logLevel());
        return merged;
    }

}
