package com.unreasonent.ds.acceptance.framework.driver;

import io.github.unacceptable.dropwizard.context.ApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ApiDriver {
    private final ApplicationContext<?> app;
    private final Client client;
    private final WebTarget rootTarget;

    public ApiDriver(ApplicationContext<?> app) {
        this.app = app;
        this.client = ClientBuilder.newClient();
        this.rootTarget = this.client.target(app.url());
    }

    public void assertNotFound() {
        assertThat(
                this.request()
                        .get()
                        .getStatus(),
                equalTo(404));
    }

    private Invocation.Builder request() {
        return this.target().request();
    }

    private WebTarget target() {
        return this.rootTarget;
    }
}
