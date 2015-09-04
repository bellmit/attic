package com.loginbox.app.version.filters;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VersionFilterTest {
    private ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
    private ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
    private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

    @Before
    public void wireMocks() {
        when(responseContext.getHeaders()).thenReturn(headers);
    }

    @Test
    public void injectsVersionHeader() throws IOException {
        VersionFilter filter = new VersionFilter("VERSION");
        filter.filter(requestContext, responseContext);

        assertThat(headers.get("X-Version"), contains("VERSION"));
    }
}
