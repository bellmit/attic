package io.github.unacceptable.selenium.driver;

import io.github.unacceptable.selenium.context.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * A base for Selenium-based page drivers. Provides page drivers with a nicer API for working with the underlying
 * Selenium context, while delegating configuration and driver initialization to the context itself.
 */
public abstract class SeleniumDriver {
    protected final SeleniumContext context;

    public SeleniumDriver(SeleniumContext context) {
        this.context = context;
    }

    /**
     * Provides a Selenium {@link org.openqa.selenium.WebDriver}, creating one if necessary from the underlying {@link
     * SeleniumContext}.
     *
     * @return a WebDriver instance obtained from the underlying system driver.
     */
    protected WebDriver webDriver() {
        return context.webDriver();
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
