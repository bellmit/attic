package com.loginbox.app.acceptance.framework.page;

import com.loginbox.app.acceptance.framework.context.TestContext;
import com.loginbox.app.acceptance.framework.driver.LandingPageDriver;

import java.util.function.Supplier;

/**
 * DSL pack for interacting with the landing page.
 */
public class LandingPage extends Dsl<LandingPageDriver> {
    public LandingPage(Supplier<? extends LandingPageDriver> driverFactory, TestContext testContext) {
        super(driverFactory, testContext);
    }

    /**
     * Verifies that the browser is currently displaying the landing page.
     */
    public void ensureLandingPageShown() {
        driver().assertLandingPageShown();
    }
}
