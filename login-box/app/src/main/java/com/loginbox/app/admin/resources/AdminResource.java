package com.loginbox.app.admin.resources;

import com.loginbox.app.admin.api.AdminPage;
import com.loginbox.app.mime.Types;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Processes the core admin UI and API.
 */
@Path("/admin/")
public class AdminResource {
    /**
     * URI builder for {@link com.loginbox.app.admin.resources.AdminResource}. Given a {@link javax.ws.rs.core.UriInfo}
     * obtained from a request, this can reconstruct the URIs of each method in the resource class. This is generally
     * more testing-friendly than directly constructing URIs in-situ, and is more likely to be kept up to date as this
     * class evolves.
     */
    public static class Uris {
        public URI get(UriInfo uriInfo) {
            return uriInfo.getBaseUriBuilder()
                    .path(AdminResource.class)
                    .build();
        }
    }

    public static Uris URIS = new Uris();

    /**
     * Displays the admin UI.
     *
     * @return a populated AdminPage.
     */
    @GET
    @Produces({Types.TEXT_HTML, Types.APPLICATION_LOGIN_BOX})
    public AdminPage get() {
        return new AdminPage();
    }
}
