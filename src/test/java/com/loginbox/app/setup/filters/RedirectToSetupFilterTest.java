package com.loginbox.app.setup.filters;

import com.loginbox.app.directory.bootstrap.Gatekeeper;
import com.loginbox.app.setup.resources.SetupResource;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;

import static com.loginbox.matchers.HasHttpStatusMatcher.hasHttpStatus;
import static com.loginbox.matchers.RedirectMatcher.redirectsTo;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedirectToSetupFilterTest {
    private final Object setupResource = new Object();
    private final Object nonSetupResource = new Object();

    private final Gatekeeper gatekeeper = mock(Gatekeeper.class);
    private final SetupResource.Uris uris = mock(SetupResource.Uris.class);
    private final RedirectToSetupFilter filter = new RedirectToSetupFilter(gatekeeper, uris, setupResource);

    private final UriInfo uriInfo = mock(UriInfo.class);
    private final URI setupUri = URI.create("http://example.com/setup/");
    private final ContainerRequestContext requestContext = mock(ContainerRequestContext.class);

    @Before
    public void wireMocks() {
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(uris.setupForm(uriInfo)).thenReturn(setupUri);
    }

    @Test
    public void redirectsToSetupInSetupMode() throws Exception {
        when(gatekeeper.isBootstrapped()).thenReturn(false);
        when(uriInfo.getMatchedResources()).thenReturn(Arrays.asList(nonSetupResource));
        filter.filter(requestContext);

        verify(requestContext).abortWith(argThat(redirectsTo(setupUri)));
    }

    @Test
    public void noRedirectOutsideSetupMode() throws Exception {
        when(gatekeeper.isBootstrapped()).thenReturn(true);
        when(uriInfo.getMatchedResources()).thenReturn(Arrays.asList(nonSetupResource));
        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @Test
    public void allowsRequestsToSetupInSetupMode() throws Exception {
        when(gatekeeper.isBootstrapped()).thenReturn(false);
        when(uriInfo.getMatchedResources()).thenReturn(Arrays.asList(setupResource));
        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @Test
    public void blocksSetupOutsideSetupMode() throws Exception {
        when(gatekeeper.isBootstrapped()).thenReturn(true);
        when(uriInfo.getMatchedResources()).thenReturn(Arrays.asList(setupResource));
        filter.filter(requestContext);

        verify(requestContext).abortWith(argThat(hasHttpStatus(Response.Status.FORBIDDEN)));
    }
}