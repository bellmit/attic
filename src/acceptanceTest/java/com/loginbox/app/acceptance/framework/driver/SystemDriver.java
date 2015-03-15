package com.loginbox.app.acceptance.framework.driver;


import com.loginbox.app.acceptance.framework.Lazily;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class SystemDriver {
    private static final String BASE_URL = System.getProperty(
            "application.base.url",
            "http://localhost:5000/");
    private static final int SELENIUM_WAIT_SECONDS = Integer.getInteger("selenium.timeout", 2);

    private WebDriver webDriver = null;
    private WebUiDriver webUiDriver = null;

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

    public void shutdown() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    public WebUiDriver webUiDriver() {
        return webUiDriver = Lazily.create(webUiDriver, () -> new WebUiDriver(this));
    }

    public String baseUrl() {
        return BASE_URL;
    }

    public TestRule rules() {
        return new SeleniumShutdownRule();
    }

    private class SeleniumShutdownRule extends ExternalResource {
        @Override
        protected void after() {
            shutdown();
        }
    }
}
