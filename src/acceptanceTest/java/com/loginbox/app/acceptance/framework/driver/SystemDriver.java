package com.loginbox.app.acceptance.framework.driver;

import com.loginbox.app.LoginBox;
import com.loginbox.app.LoginBoxConfiguration;
import com.loginbox.app.acceptance.framework.Lazily;
import com.loginbox.app.acceptance.framework.database.DatabaseContext;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.TimeUnit;

public class SystemDriver {
    private static final int SELENIUM_WAIT_SECONDS = Integer.getInteger("selenium.timeout", 2);
    private static final String DATABASE_ADMIN_URL = System.getProperty("database.adminUrl", "jdbc:postgresql://localhost/postgres");
    private static final String DATABASE_USERNAME = System.getProperty("database.username", "postgres");
    private static final String DATABASE_PASSWORD = System.getProperty("database.password");

    private static final ConfigOverride LOG_THRESHOLD = ConfigOverride.config("logging.appenders[0].threshold", System.getProperty("app.logging.threshold", "WARN"));

    private final DatabaseContext databaseContext = new DatabaseContext(DATABASE_ADMIN_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

    private final DropwizardAppRule<LoginBoxConfiguration> appRule = new DropwizardAppRule<>(LoginBox.class, null,
            LOG_THRESHOLD,
            databaseContext.databaseUrl(),
            databaseContext.user(),
            databaseContext.password());
    private final DriverShutdownRule driverShutdownRule = new DriverShutdownRule();

    private String baseUrl = null;
    private WebDriver webDriver = null;
    private Client publicApiClient = null;
    private WebUiDriver webUiDriver = null;
    private PublicApiDriver publicApiDriver = null;
    private SetupPageDriver setupPageDriver = null;
    private AdminPageDriver adminPageDriver = null;
    private LandingPageDriver landingPageDriver = null;
    private ErrorPageDriver errorPageDriver = null;

    /**
     * Returns the current session's {@link org.openqa.selenium.WebDriver}, starting it if necessary. Callers
     * <strong>must not</strong> retain references to the returned driver: calls to {@link #shutdown()} must be able to
     * discard the driver completely.
     * <p>
     * This is bad:
     * <pre>
     *     private WebDriver webDriver = systemDriver.webDriver();
     *     // ...
     *     this.webDriver.findElement(...);
     * </pre>
     * Instead, do this:
     * <pre>
     *     systemDriver.webDriver().findElement(...);
     * </pre>
     * Note that this is already taken care of in {@link SeleniumDriver}. Drivers which extend that class will do the
     * right thing by default.
     *
     * @return the current session's Selenium driver.
     */
    public WebDriver webDriver() {
        return webDriver = Lazily.create(webDriver, () -> {
            WebDriver webDriver = new FirefoxDriver();
            webDriver.manage()
                    .timeouts()
                    .implicitlyWait(SELENIUM_WAIT_SECONDS, TimeUnit.SECONDS);
            webDriver.manage()
                    .window()
                    .setSize(new Dimension(1024, 768));
            return webDriver;
        });
    }

    public Client publicApiClient() {
        return publicApiClient = Lazily.create(publicApiClient, () -> ClientBuilder.newClient());
    }

    public void shutdown() {
        quitWebDriver();
        quitPublicApiDriver();
    }

    public void quitPublicApiDriver() {
        if (publicApiClient != null) {
            publicApiClient.close();
            publicApiClient = null;
        }
    }

    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    public WebUiDriver webUiDriver() {
        return webUiDriver = Lazily.create(webUiDriver, () -> new WebUiDriver(this));
    }

    public String baseUrl() {
        return baseUrl = Lazily.create(baseUrl, () -> String.format("http://localhost:%d/", appRule.getLocalPort()));
    }

    public SetupPageDriver setupPageDriver() {
        return setupPageDriver = Lazily.create(setupPageDriver, () -> new SetupPageDriver(this));
    }

    public AdminPageDriver adminPageDriver() {
        return adminPageDriver = Lazily.create(adminPageDriver, () -> new AdminPageDriver(this));
    }

    public LandingPageDriver landingPageDriver() {
        return landingPageDriver = Lazily.create(landingPageDriver, () -> new LandingPageDriver(this));
    }

    public ErrorPageDriver errorPageDriver() {
        return errorPageDriver = Lazily.create(errorPageDriver, () -> new ErrorPageDriver(this));
    }

    public TestRule rules() {
        return RuleChain
                .outerRule(databaseContext.rules())
                .around(appRule)
                .around(driverShutdownRule);
    }

    public PublicApiDriver publicApiDriver() {
        return publicApiDriver = Lazily.create(publicApiDriver, () -> new PublicApiDriver(this, baseUrl()));
    }

    private class DriverShutdownRule extends ExternalResource {
        @Override
        protected void after() {
            shutdown();
        }
    }
}
