package com.loginbox.app.landing.resources;

import com.loginbox.app.landing.api.LandingPage;
import com.loginbox.app.mime.Types;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Returns Login Box's default landing page. This is a temporary arrangement (yes, yes, famous last words) until user
 * login exists.
 */
@Path("/")
public class LandingResource {
    /**
     * Displays the default landing page.
     *
     * @return the default landing page.
     */
    @GET
    @Produces({Types.TEXT_HTML, Types.APPLICATION_LOGIN_BOX})
    public LandingPage hello() {
        return new LandingPage();
    }
}
