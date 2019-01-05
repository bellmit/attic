package com.loginbox.app.acceptance.framework.driver;

import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.dropwizard.driver.SeleniumDriver;
import io.github.unacceptable.selenium.context.SeleniumContext;

public class WebUiDriver extends SeleniumDriver {
    public WebUiDriver(SeleniumContext selenium, ApplicationContext<?> app) {
        super(selenium, app);
    }

    public void open() {
        webDriver().get(app.url());
    }

    public void close() {
        context.quit();
    }
}
