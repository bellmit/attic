package com.unreasonent.ds.acceptance.framework.page;

import com.unreasonent.ds.acceptance.framework.context.TestContext;
import com.unreasonent.ds.acceptance.framework.driver.SystemDriver;
import com.unreasonent.ds.acceptance.framework.driver.WebUiDriver;
import io.github.unacceptable.dsl.SimpleDsl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * DSL for manipulating the web UI as a whole. Provides actions and assertions that can be performed regardless of the
 * application's state, such as opening the application.
 * <p>
 * At the start of a test, this represents a new, unauthenticated user session. During the the test, this remains
 * attached to a web browser profile, to simulate a user who can navigate the site, close and open their browser, and
 * otherwise perform normal interactions.
 */
public class WebUi extends SimpleDsl<WebUiDriver, TestContext> {
    public WebUi(SystemDriver systemDriver, TestContext testContext) {
        super(systemDriver::webUiDriver, testContext);
    }

    /**
     * Navigates to the application's landing page, opening a browser if necessary.
     */
    public void open() {
        driver().open();
    }

    /**
     * Exit the web UI, closing the browser. This will also discard all session credentials, but not persistent
     * credentials. A new session can be started using <code>webUi.open()</code>.
     */
    public void close() {
        driver().close();
    }

    /**
     * Asserts that the browser is displaying the landing page. This is based on user-facing behaviours
     * (landing-page-specific text, mainly).
     */
    public void ensureLandingPageShown() {
        assertThat(driver().getAppText(), equalTo("hi, you"));
    }
}
