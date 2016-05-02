package com.unreasonent.ds.acceptance.framework.driver;

import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.dropwizard.driver.SeleniumDriver;
import io.github.unacceptable.selenium.context.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebUiDriver extends SeleniumDriver {
    public WebUiDriver(SeleniumContext selenium, ApplicationContext<?> app) {
        super(selenium, app);
    }

    public void open() {
        webDriver().get(app.url());
    }

    public void close() {
        webDriver().close();
    }

    public String getAppText() {
        return findAppRoot().getText();
    }

    private WebElement findAppRoot() {
        return findElement(By.id("app"));
    }
}
