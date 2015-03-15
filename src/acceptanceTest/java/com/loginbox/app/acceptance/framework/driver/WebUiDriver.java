package com.loginbox.app.acceptance.framework.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebUiDriver extends SeleniumDriver {
    public WebUiDriver(SystemDriver systemDriver) {
        super(systemDriver);
    }

    public void open() {
        webDriver().get(systemDriver.baseUrl());
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
