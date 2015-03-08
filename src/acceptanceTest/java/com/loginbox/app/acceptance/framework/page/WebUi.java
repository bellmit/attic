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
    public WebUi(SystemDriver systemDriver, TestContext testContext) {
        super(systemDriver::webUiDriver, testContext);
    }

    /**
     * Navigates to the application's landing page, opening a browser if necessary.
     */
    public void open() {
        driver().open();
    }

    public void ensureDemoMessageShown() {
        assertThat(driver().getDemoMessage(), is("Hello, world!"));
    }
}
