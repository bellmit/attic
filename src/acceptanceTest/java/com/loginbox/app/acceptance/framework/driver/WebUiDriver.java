package com.loginbox.app.acceptance.framework.driver;

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
        systemDriver.quitWebDriver();
    }
}
