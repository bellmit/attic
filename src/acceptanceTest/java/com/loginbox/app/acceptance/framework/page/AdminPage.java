package com.loginbox.app.acceptance.framework.page;

import com.loginbox.app.acceptance.framework.context.TestContext;
import com.loginbox.app.acceptance.framework.driver.AdminPageDriver;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * DSL pack for manipulating the app configuration via its administration panel.
 */
public class AdminPage extends Dsl<AdminPageDriver> {
    public AdminPage(Supplier<AdminPageDriver> driverFactory, TestContext testContext) {
        super(driverFactory, testContext);
    }

    /**
     * Checks that the browser is currently on the admin landing page.
     */
    public void ensureAdminShown() {
        assertThat(driver().getAdminHeading(), is("Administer Login Box"));
    }

    /**
     * Directly open the admin user interface.
     *
     * @deprecated This is only possible because there's no access control on the admin user interface. This method will
     * disappear once it's possible to navigate to the admin UI as a non-admin user.
     */
    @Deprecated
    public void open() {
        driver().open();
    }
}
