package com.loginbox.app.acceptance.framework.page;

import com.loginbox.app.acceptance.framework.context.TestContext;
import com.loginbox.app.acceptance.framework.driver.SystemDriver;
import com.loginbox.app.acceptance.framework.driver.WebUiDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * DSL for manipulating the web UI as a whole. Provides actions and assertions that can be performed regardless of the
 * application's state, such as opening the application.
 */
public class WebUi extends Dsl<WebUiDriver> {
    /**
     * Nested DSL for configuring the app.
     */
    public final AdminPage adminPage;

    /**
     * Nested DSL for the landing page.
     */
    public final LandingPage landingPage;

    public WebUi(SystemDriver systemDriver, TestContext testContext) {
        super(systemDriver::webUiDriver, testContext);
        this.adminPage = new AdminPage(systemDriver::adminPageDriver, testContext);
        this.landingPage = new LandingPage(systemDriver::landingPageDriver, testContext);
    }

    /**
     * Navigates to the application's landing page, opening a browser if necessary.
     */
    public void open() {
        driver().open();
    }

}
