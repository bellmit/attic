package com.loginbox.app.acceptance.framework.page;

import com.lmax.simpledsl.DslParam;
import com.lmax.simpledsl.DslParams;
import com.lmax.simpledsl.OptionalParam;
import com.loginbox.app.acceptance.framework.context.TestContext;
import com.loginbox.app.acceptance.framework.driver.SetupPageDriver;
import io.github.unacceptable.dsl.SimpleDsl;

import java.util.function.Supplier;

/**
 * Provides actions and assertions for the app setup process.
 */
public class SetupPage extends SimpleDsl<SetupPageDriver, TestContext> {
    static DslParam usernameParam() {
        return new OptionalParam("username")
                .setDefault("admin-user");
    }

    static DslParam emailParam() {
        return new OptionalParam("email")
                .setDefault("admin-user@example.com");
    }

    static DslParam passwordParam() {
        return new OptionalParam("password")
                .setDefault("admin-user");
    }

    public SetupPage(Supplier<SetupPageDriver> driverFactory, TestContext testContext) {
        super(driverFactory, testContext);
    }

    /**
     * Checks that the browser is currently on the setup welcome page.
     */
    public void ensureWelcomeShown() {
        driver().ensureWelcomeMessagePresent();
    }

    /**
     * Enters a <var>username</var> into the setup page's username field.
     */
    public void enterUsername(String... args) {
        DslParams params = new DslParams(
                args,
                usernameParam()
        );
        String username = testContext.usernames.resolve(params.value("username"));

        enterUsernameValues(username);
    }

    void enterUsernameValues(String username) {
        driver().enterUsername(username);
    }

    /**
     * Enters an <var>email</var> into the setup page's contact email address field.
     */
    public void enterContactEmail(String... args) {
        DslParams params = new DslParams(
                args,
                emailParam()
        );
        String emailAddress = testContext.emailAddresses.resolve(params.value("email"));

        enterContactEmailValues(emailAddress);
    }

    void enterContactEmailValues(String username) {
        driver().enterContactEmail(username);
    }

    /**
     * Enters a <var>password</var> into the setup page's password field.
     */
    public void enterPassword(String... args) {
        DslParams params = new DslParams(
                args,
                passwordParam()
        );
        String password = testContext.passwords.resolve(params.value("password"));

        enterPasswordValues(password);
    }

    void enterPasswordValues(String password) {
        driver().enterPassword(password);
    }

    /**
     * Enters a <var>password</var> into the setup page's password confirmation field.
     */
    public void confirmPassword(String... args) {
        DslParams params = new DslParams(
                args,
                passwordParam()
        );
        String password = testContext.passwords.resolve(params.value("password"));

        confirmPasswordValues(password);
    }

    void confirmPasswordValues(String password) {
        driver().enterPasswordConfirmation(password);
    }

    /**
     * Attempts to configure the app using the current setup fields.
     */
    public void clickCompleteSetup() {
        driver().clickCompleteSetup();
    }
}
