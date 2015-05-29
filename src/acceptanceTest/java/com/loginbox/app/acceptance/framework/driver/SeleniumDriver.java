package com.loginbox.app.acceptance.framework.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * A base for Selenium-based test drivers. Provides on-demand initialization of the Selenium infrastructure, avoiding
 * the cost of starting Selenium and opening a browser until a test actually uses it.
 */
public abstract class SeleniumDriver {
    protected final SystemDriver systemDriver;
    private WebDriver webDriver = null;

    public SeleniumDriver(SystemDriver systemDriver) {
        this.systemDriver = systemDriver;
    }

    /**
     * Provides a Selenium {@link org.openqa.selenium.WebDriver}, creating one if necessary from the underlying {@link
     * SystemDriver}.
     *
     * @return a WebDriver instance obtained from the underlying system driver.
     */
    protected WebDriver webDriver() {
        return systemDriver.webDriver();
    }

    /**
     * Shorthand for <code>webDriver().findElement(by)</code>. This is a common case in Selenium-based tests, and saving
     * some keystrokes helps.
     *
     * @see #webDriver()
     * @see org.openqa.selenium.WebDriver#findElement(org.openqa.selenium.By)
     */
    protected WebElement findElement(By by) {
        return webDriver()
                .findElement(by);
    }

    /**
     * Shorthand for <code>findElement(By.tagName("h1"))</code>. This is a common case for inspecting the page's title
     * heading.
     *
     * @see #findElement(org.openqa.selenium.By)
     */
    protected WebElement findPageHeading() {
        return findElement(By.tagName("h1"));
    }
}
