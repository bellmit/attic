package io.github.unacceptable.selenium.context;

import io.github.unacceptable.lazy.Lazily;
import io.github.unacceptable.selenium.driver.SeleniumDriver;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class SeleniumContext {
    private static final int TIMEOUT_SECONDS = Integer.getInteger("selenium.timeout", 2);
    private static final int BROWSER_WIDTH = Integer.getInteger("selenium.window.width", 1024);
    private static final int BROWSER_HEIGHT = Integer.getInteger("selenium.window.height", 768);

    private class DriverShutdownRule extends ExternalResource {
        @Override
        protected void after() {
            quit();
        }
    }

    private WebDriver webDriver = null;

    /**
     * Returns the current session's {@link org.openqa.selenium.WebDriver}, starting it if necessary. Callers
     * <strong>must not</strong> retain references to the returned driver: calls to {@link #quit()} must be able to
     * discard the driver completely.
     * <p>
     * This is bad:
     * <pre>
     *     private WebDriver webDriver = context.webDriver();
     *     // ...
     *     this.webDriver.findElement(...);
     * </pre>
     * Instead, do this:
     * <pre>
     *     context.webDriver().findElement(...);
     * </pre>
     * Note that this is already taken care of in {@link io.github.unacceptable.selenium.driver.SeleniumDriver}. Drivers
     * which extend that class and use its {@link SeleniumDriver#webDriver()} method will do the right thing by
     * default.
     *
     * @return the current session's Selenium driver.
     */
    public WebDriver webDriver() {
        return webDriver = Lazily.create(webDriver, this::newDriver);
    }

    /**
     * Quit the current session, if open. This will also discard the driver; subsequent calls to {@link #webDriver()}
     * will create new, fresh driver instances.
     */
    public void quit() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    /**
     * Returns test rules that ensure that Selenium is torn down after each test.
     *
     * @return a shutdown rule that invokes this driver's {@link #quit()} method after each test.
     */
    public TestRule rules() {
        return new DriverShutdownRule();
    }

    /**
     * Create the underlying {@link WebDriver} instance. Configuration (timeouts, dimensions, and so on) will be applied
     * to the returned WebDriver automatically; this method body should normally be a single {@code return new …;}
     * statement.
     * <p>
     * The default implementation returns a {@link FirefoxDriver}.
     *
     * @return a new, unconfigured {@code WebDriver}.
     */
    protected WebDriver createDriver() {
        return new FirefoxDriver();
    }

    private WebDriver newDriver() {
        WebDriver webDriver = createDriver();
        webDriver.manage()
                .timeouts()
                .implicitlyWait(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        webDriver.manage()
                .window()
                .setSize(new Dimension(BROWSER_WIDTH, BROWSER_HEIGHT));
        return webDriver;
    }
}
