package com.loginbox.app.acceptance.framework.page;

import com.loginbox.app.acceptance.framework.context.TestContext;
import com.loginbox.app.acceptance.framework.driver.PublicApiDriver;
import com.loginbox.app.acceptance.framework.driver.SystemDriver;
import io.github.unacceptable.dsl.SimpleDsl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * DSL describing actions available through the API without authentication.
 */
public class PublicApi extends SimpleDsl<PublicApiDriver, TestContext> {
    public PublicApi(SystemDriver systemDriver, TestContext testContext) {
        super(systemDriver::publicApiDriver, testContext);
    }

    /**
     * Ensures that the version header is present in requests by checking the root API URL.
     */
    public void ensureVersionPresent() {
        assertThat(driver().getVersion(), is(not(nullValue())));
    }
}
