package com.unreasonent.ds.acceptance.framework.driver;

import com.unreasonent.ds.acceptance.framework.rest.ApiContext;

import javax.ws.rs.client.Invocation;

public class ApiDriver {
    private final ApiContext apiContext;

    public ApiDriver(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    protected Invocation.Builder request(String path) {
        return apiContext.request(path);
    }
}
