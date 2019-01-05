package com.loginbox.app.acceptance.framework.driver;

import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.dropwizard.driver.SeleniumDriver;
import io.github.unacceptable.selenium.context.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AdminPageDriver extends SeleniumDriver {
    public AdminPageDriver(SeleniumContext selenium, ApplicationContext<?> app) {
        super(selenium, app);
    }

    public String getAdminHeading() {
        return findPageHeading()
                .getText();
    }

    @Deprecated
    public void open() {
        webDriver().navigate().to(app.url("/admin"));
    }

    private WebElement findPageHeading() {
        return findElement(By.tagName("h1"));
    }
}
