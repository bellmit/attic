package com.unreasonent.ds.frontend.resources;

import com.unreasonent.ds.frontend.api.FrontendView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Provides a fallback route that serves the front-end HTML, in cases where no other route matches the request. This
 * relies on being the only route that provides {@code text/html} content, as well as on ordering.
 */
@Path("/{path:.*}")
public class Frontend {
    @GET
    @Produces("text/html")
    public FrontendView get() {
        return new FrontendView();
    }
}
