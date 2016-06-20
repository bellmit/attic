package com.unreasonent.ds.acceptance.framework.dsl;

import com.lmax.simpledsl.DslParam;
import com.lmax.simpledsl.DslParams;
import com.lmax.simpledsl.RequiredParam;
import com.unreasonent.ds.acceptance.framework.context.TestContext;
import com.unreasonent.ds.acceptance.framework.driver.SystemDriver;
import com.unreasonent.ds.acceptance.framework.rest.ApiContext;

public class Api {
    static DslParam userId() {
        return new RequiredParam("userId");
    }

    private final TestContext testContext;
    private final ApiContext apiContext;

    public Api(SystemDriver systemDriver, TestContext testContext) {
        this.testContext = testContext;
        this.apiContext = systemDriver.apiContext;

        this.squad = new Squad(systemDriver, testContext);
    }

    public final Squad squad;

    public void authenticateAs(String... args) {
        DslParams params = new DslParams(args,
                userId()
        );

        String userId = testContext.usernames.resolve(params.value("userId"));
        authenticateAsValues(userId);
    }

    void authenticateAsValues(String userId) {
        String jwtToken = testContext.tokens.resolve(userId);
        apiContext.useToken(jwtToken);
    }
}
