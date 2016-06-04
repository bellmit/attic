package com.unreasonent.ds.auth.resources;

import com.unreasonent.ds.auth.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("whoami")
public class WhoAmI {
    @GET
    @Produces("application/json")
    public User whoAmI(@Auth User self) {
        return self;
    }
}
