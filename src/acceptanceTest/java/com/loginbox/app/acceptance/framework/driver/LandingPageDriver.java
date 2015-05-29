package com.loginbox.app.acceptance.framework.driver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LandingPageDriver extends SeleniumDriver {
    public LandingPageDriver(SystemDriver systemDriver) {
        super(systemDriver);
    }

    public void assertLandingPageShown() {
        assertThat(findPageHeading().getText(), is("Login Box"));
    }
}
