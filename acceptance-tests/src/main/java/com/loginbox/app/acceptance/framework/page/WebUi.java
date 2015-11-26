package com.loginbox.app.acceptance.framework.page;

import com.lmax.simpledsl.DslParams;
import com.loginbox.app.acceptance.framework.context.TestContext;
import com.loginbox.app.acceptance.framework.driver.SystemDriver;
import com.loginbox.app.acceptance.framework.driver.WebUiDriver;
import io.github.unacceptable.dsl.SimpleDsl;

/**
 * DSL for manipulating the web UI as a whole. Provides actions and assertions that can be performed regardless of the
 * application's state, such as opening the application.
 */
public class WebUi extends SimpleDsl<WebUiDriver, TestContext> {
    /**
     * Nested DSL for the setup UI.
     */
    public final SetupPage setupPage;

    /**
     * Nested DSL for configuring the app.
     */
    public final AdminPage adminPage;

    /**
     * Nested DSL for the landing page.
     */
    public final LandingPage landingPage;

    /**
     * Nested DSL for Jetty error pages.
     */
    public ErrorPage errorPage;

    private final SystemDriver systemDriver;

    public WebUi(SystemDriver systemDriver, TestContext testContext) {
        super(systemDriver::webUiDriver, testContext);
        this.systemDriver = systemDriver;
        this.setupPage = new SetupPage(systemDriver::setupPageDriver, testContext);
        this.adminPage = new AdminPage(systemDriver::adminPageDriver, testContext);
        this.landingPage = new LandingPage(systemDriver::landingPageDriver, testContext);
        this.errorPage = new ErrorPage(systemDriver::errorPageDriver, testContext);
    }

    /**
     * Navigates to the application's landing page, opening a browser if necessary.
     */
    public void open() {
        driver().open();
    }

    /**
     * Configure the app. This presumes that the browser is already displaying the setup UI.
     */
    public void setupApp(String... args) {
        DslParams params = new DslParams(
                args,
                SetupPage.usernameParam(),
                SetupPage.emailParam(),
                SetupPage.passwordParam()
        );
        String username = testContext.usernames.resolve(params.value("username"));
        String email = testContext.emailAddresses.resolve(params.value("email"));
        String password = testContext.passwords.resolve(params.value("password"));

        setupPage.enterUsernameValues(username);
        setupPage.enterContactEmailValues(email);
        setupPage.enterPasswordValues(password);
        setupPage.confirmPasswordValues(password);
        setupPage.clickCompleteSetup();
    }

    /**
     * Exit the web UI, closing the browser. This will also discard all session credentials, but not persistent
     * credentials. A new session can be started using <code>webUi.open()</code>.
     */
    public void close() {
        driver().close();
    }
}
