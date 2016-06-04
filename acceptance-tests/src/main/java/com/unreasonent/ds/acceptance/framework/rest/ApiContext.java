package com.unreasonent.ds.acceptance.framework.rest;

import io.github.unacceptable.dropwizard.context.ApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.util.function.Function;

public class ApiContext {
    private final ApplicationContext<?> app;
    private final Client client;
    private Function<Invocation.Builder, Invocation.Builder> headers = builder -> builder;

    public ApiContext(ApplicationContext<?> app) {
        this.app = app;
        this.client = ClientBuilder
                .newBuilder()
                .build();
    }

    public Invocation.Builder request(String... paths) {
        return headers.apply(this.target(paths)
                .request());
    }

    private WebTarget target(String... paths) {
        WebTarget target = client.target(app.url());
        for (String path : paths) {
            target = target.path(path);
        }
        return target;
    }

    /**
     * Use a specific token when issuing API requests. All subsequent requests will use this token.
     *
     * @param jwtToken
     *         the token to use.
     */
    public void useToken(String jwtToken) {
        this.headers = builder -> builder.header("Authorization", String.format("Bearer %s", jwtToken));
    }
}
