package com.loginbox.app.version.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class VersionFilter implements ContainerResponseFilter {
    private static final String VERSION_HEADER = "X-Version";
    private final String version;

    public VersionFilter(String version) {
        this.version = version;
    }

    /**
     * Injects the configured version info into the response's headers. This will add a new value to the {@value
     * #VERSION_HEADER} header, setting it if absent.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        injectHeaders(responseContext.getHeaders());
    }

    private void injectHeaders(MultivaluedMap<String, Object> headers) {
        headers.add(VERSION_HEADER, version);
    }
}
