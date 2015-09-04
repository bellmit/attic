package com.loginbox.app.acceptance.framework.page;

import com.lmax.simpledsl.DslParams;
import com.lmax.simpledsl.RequiredParam;
import com.loginbox.app.acceptance.framework.context.TestContext;
import com.loginbox.app.acceptance.framework.driver.ErrorPageDriver;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * A DSL for working with Jetty's built-in error pages. Generally, prefer domain-specific errors, but this is an okay
 * fallback.
 */
public class ErrorPage extends Dsl<ErrorPageDriver> {
    public ErrorPage(Supplier<? extends ErrorPageDriver> driverFactory, TestContext testContext) {
        super(driverFactory, testContext);
    }

    /**
     * Ensure that a Jetty error page is shown. This DSL method has two required parameters:
     * <ul>
     *     <li><var>error</var>: the error type message shown at the top of the page</li>
     *     <li><var>message</var>: the error message body shown in the center of the page</li>
     * </ul>
     */
    public void ensure(String... args) {
        DslParams params = new DslParams(
                args,
                new RequiredParam("error"),
                new RequiredParam("message")
        );

        String error = params.value("error");
        String message = params.value("message");

        assertThat(driver().getErrorHeading(), is(error));
        assertThat(driver().getMessage(), is(message));
    }
}
