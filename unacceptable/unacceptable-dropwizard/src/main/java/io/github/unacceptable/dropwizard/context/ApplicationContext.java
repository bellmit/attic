package io.github.unacceptable.dropwizard.context;

import com.google.common.base.Optional;
import com.google.common.collect.ObjectArrays;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.github.unacceptable.lazy.Lazily;
import org.junit.rules.TestRule;

import javax.annotation.Nullable;

import static io.github.unacceptable.lazy.Lazily.systemProperty;

/**
 * Starts a Dropwizard application around each test run, unless a previously-running application's URL has been
 * provided.
 * <p>
 * To target tests to an existing app, set the {@code app.url} system property. If not set, this context will boot an
 * instance of your application for the duration of the test, then shut it down.
 * <p>
 * When booting an application, the configuration overrides will be applied. By default, the only override applied sets
 * the logging level to {@link #logLevel()}, which will suppress the vast sea of bootup and shutdown diagnostic
 * messages.
 *
 * @param <C>
 *         the configuration type of the target Dropwizard application.
 */
public class ApplicationContext<C extends Configuration> {

    private final Class<? extends Application<C>> applicationClass;
    private final String configPath;
    private final Optional<String> customPropertyPrefix;
    private final ConfigOverride[] configOverrides;
    private ApplicationState<C> applicationState = null;
    private String logLevel = null;

    /**
     * Create a context by specifying only the dropwizard {@link Application} subclass.  This will use a null
     * configuration path, and only the default configuration override for logging level. Effectively, this delegates to
     * the similar {@link io.dropwizard.testing.junit.DropwizardAppRule} constructor.
     *
     * @param applicationClass
     *         the Dropwizard application's main class, used to boot the application if needed.
     */
    public ApplicationContext(Class<? extends Application<C>> applicationClass) {
        this(applicationClass, (String) null);
    }

    /**
     * Create a context by specifying the dropwizard {@link Application} subclass, a (nullable) configuration path, and
     * an optional list of configuration overrides.  These config overrides will have the default log level override
     * added to them, but can override it. Effectively, this delegates to the similar {@link
     * io.dropwizard.testing.junit.DropwizardAppRule} constructor.
     *
     * @param applicationClass
     *         the Dropwizard application's main class, used to boot the application if needed.
     * @param configPath
     *         the path to the application's config file (if any), or {@code null} if the default configuration is
     *         sufficient to boot the app for testing.
     * @param configOverrides
     *         any configuration overrides needed to set the app up for testing. By default, an additional override
     *         suppressing INFO log messages will be applied to the application.
     */
    public ApplicationContext(Class<? extends Application<C>> applicationClass,
                              @Nullable String configPath,
                              ConfigOverride... configOverrides) {
        this(applicationClass, configPath, Optional.<String>absent(), configOverrides);
    }

    /**
     * Create a context similarily to the previous constructor, but with a custom property prefix. Effectively, this
     * delegates to the similar {@link io.dropwizard.testing.junit.DropwizardAppRule} constructor.
     *
     * @param applicationClass
     *         the Dropwizard application's main class, used to boot the application if needed.
     * @param configPath
     *         the path to the application's config file (if any), or {@code null} if the default configuration is
     *         sufficient to boot the app for testing.
     * @param customPropertyPrefix
     *         the property prefix for configuration properties used in this app. For details, see {@link
     *         DropwizardTestSupport}.
     * @param configOverrides
     *         any configuration overrides needed to set the app up for testing. By default, an additional override
     *         suppressing INFO log messages will be applied to the application.
     */
    public ApplicationContext(Class<? extends Application<C>> applicationClass, String configPath,
                              Optional<String> customPropertyPrefix, ConfigOverride... configOverrides) {
        this.applicationClass = applicationClass;
        this.configPath = configPath;
        this.customPropertyPrefix = customPropertyPrefix;
        this.configOverrides = configOverrides;
    }

    public boolean isExternal() {
        return getAppUrl() != null;
    }

    private String getAppUrl() {
        return System.getProperty("app.url");
    }

    private ApplicationState<C> applicationState() {
        return applicationState = Lazily.create(applicationState, this::detectApplicationState);
    }

    private ApplicationState<C> detectApplicationState() {
        if (!isExternal()) {
            return new OneShotApplicationState<>(dropwizardTestSupport());
        }
        return new ExternalApplicationState<>(getAppUrl());
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

    private ConfigOverride[] appendLogLevelOverride(final ConfigOverride[] configOverrides) {
        return ObjectArrays.concat(
                ConfigOverride.config("logging.level", logLevel()),
                configOverrides);
    }

}
