package com.loginbox.app.acceptance.framework.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ErrorPageDriver extends SeleniumDriver {
    public ErrorPageDriver(SystemDriver systemDriver) {
        super(systemDriver);
    }

    public String getErrorHeading() {
        return findErrorHeading()
                .getText();
    }

    private WebElement findErrorHeading() {
        return findElement(By.tagName("h2"));
    }

    public String getMessage() {
        return findMessage()
                .getText()
                .trim();
    }

    private WebElement findMessage() {
        return findElement(By.tagName("pre"));
    }
}
