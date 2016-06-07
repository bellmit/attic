package com.unreasonent.ds.acceptance.framework.driver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unreasonent.ds.acceptance.framework.rest.ApiContext;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class WhoAmIDriver {
    private final ApiContext apiContext;

    public WhoAmIDriver(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    public void assertLoggedInAs(String username) {
        Response whoAmIResponse = makeWhoAmIRequest();
        assertThat(whoAmIResponse.getStatusInfo(), equalTo(Response.Status.OK));

        User user = whoAmIResponse.readEntity(User.class);
        assertThat(user.userId, equalTo(username));
    }

    public void assertLoggedOut() {
        Response whoAmIResponse = makeWhoAmIRequest();
        assertThat(whoAmIResponse.getStatusInfo(), equalTo(Response.Status.UNAUTHORIZED));
    }

    private static class User {
        @JsonProperty
        public String userId;
    }

    private Response makeWhoAmIRequest() {
        return apiContext
                .request("whoami")
                .get();
    }
}
