package com.loginbox.app.acceptance.framework.driver;

import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.dropwizard.driver.SeleniumDriver;
import io.github.unacceptable.selenium.context.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LandingPageDriver extends SeleniumDriver {
    public LandingPageDriver(SeleniumContext selenium, ApplicationContext<?> app) {
        super(selenium, app);
    }

    public void assertLandingPageShown() {
        assertThat(findPageHeading().getText(), is("Login Box"));
    }

    private WebElement findPageHeading() {
        return findElement(By.tagName("h1"));
    }
}
