package com.unreasonent.ds.acceptance.framework.dsl;

import com.lmax.simpledsl.DslParams;
import com.unreasonent.ds.acceptance.framework.context.TestContext;
import com.unreasonent.ds.acceptance.framework.driver.SystemDriver;
import com.unreasonent.ds.acceptance.framework.driver.WhoAmIDriver;
import io.github.unacceptable.dsl.SimpleDsl;

public class WhoAmI extends SimpleDsl<WhoAmIDriver, TestContext> {
    public WhoAmI(SystemDriver systemDriver, TestContext testContext) {
        super(systemDriver::whoAmIDriver, testContext);
    }

    public void ensureLoggedIn(String... args) {
        DslParams params = new DslParams(args,
                Api.userId()
        );
        String username = testContext.usernames.resolve(params.value("userId"));

        driver().assertLoggedInAs(username);
    }

    public void ensureLoggedOut() {
        driver().assertLoggedOut();
    }
}
