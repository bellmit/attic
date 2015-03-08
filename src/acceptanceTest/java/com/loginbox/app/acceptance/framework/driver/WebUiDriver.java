package com.loginbox.app.acceptance.framework.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebUiDriver extends SeleniumDriver {
    private final String baseUrl;

    public WebUiDriver(SystemDriver systemDriver, String baseUrl) {
        super(systemDriver);
        this.baseUrl = baseUrl;
    }

    public void open() {
        webDriver().get(baseUrl);
    }

    public void close() {
        webDriver().close();
        // This is technically a superset of webDriver().close(), but the repetition feels like it addresses different
        // concerns.
        systemDriver.shutdown();
    }

    public String getDemoMessage() {
        return findDemoMessage()
                .getText();
    }

    WebElement findDemoMessage() {
        return findElement(By.tagName("h1"));
    }
}
