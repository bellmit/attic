package com.loginbox.app.demo.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String index() {
        return "<!DOCTYPE html><html><head><title>Greetings!</title></head><body><h1>Hello, world!</h1></body></html>";
    }
}
