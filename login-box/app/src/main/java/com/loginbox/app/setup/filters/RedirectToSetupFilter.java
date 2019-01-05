package com.loginbox.app.setup.filters;

import com.google.common.collect.Sets;
import com.loginbox.app.directory.bootstrap.Gatekeeper;
import com.loginbox.app.setup.resources.SetupResource;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

public class RedirectToSetupFilter implements ContainerRequestFilter {
    private final Gatekeeper gatekeeper;
    private final SetupResource.Uris setupUris;
    private final Set<Object> setupResources;

    public RedirectToSetupFilter(Gatekeeper gatekeeper, SetupResource.Uris setupUris, Object... setupResources) {
        this.gatekeeper = gatekeeper;
        this.setupUris = setupUris;
        this.setupResources = Sets.newHashSet(setupResources);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            if (gatekeeper.isBootstrapped())
                operatingModeFilter(requestContext);
            else
                setupModeFilter(requestContext);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void setupModeFilter(ContainerRequestContext requestContext) {
        UriInfo uriInfo = requestContext.getUriInfo();
        for (Object resource : uriInfo.getMatchedResources())
            if (setupResources.contains(resource))
                return;
        URI setupForm = setupUris.setupForm(uriInfo);
        requestContext.abortWith(Response.seeOther(setupForm).build());
    }

    private void operatingModeFilter(ContainerRequestContext requestContext) {
        UriInfo uriInfo = requestContext.getUriInfo();
        for (Object resource : uriInfo.getMatchedResources())
            if (setupResources.contains(resource)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }
    }
}
