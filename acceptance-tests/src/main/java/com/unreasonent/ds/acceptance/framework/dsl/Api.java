package com.unreasonent.ds.acceptance.framework.dsl;

import com.unreasonent.ds.acceptance.framework.context.TestContext;
import com.unreasonent.ds.acceptance.framework.driver.ApiDriver;
import com.unreasonent.ds.acceptance.framework.driver.SystemDriver;
import io.github.unacceptable.dsl.SimpleDsl;

public class Api extends SimpleDsl<ApiDriver, TestContext> {
    public Api(SystemDriver systemDriver, TestContext testContext) {
        super(systemDriver::apiDriver, testContext);
    }

    public void ensureNotFound() {
        driver().assertNotFound();
    }
}
