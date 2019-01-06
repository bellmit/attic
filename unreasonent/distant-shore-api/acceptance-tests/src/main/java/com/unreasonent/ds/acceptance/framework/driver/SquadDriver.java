package com.unreasonent.ds.acceptance.framework.driver;

import com.unreasonent.ds.acceptance.framework.rest.ApiContext;
import com.unreasonent.ds.squad.api.Squad;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SquadDriver extends ApiDriver {
    public SquadDriver(ApiContext apiContext) {
        super(apiContext);
    }

    public void assertNotAuthorized() {
        assertThat(
                getSquad()
                        .getStatusInfo(),
                equalTo(Response.Status.UNAUTHORIZED)
        );
    }

    public void assertNotFound() {
        assertThat(
                getSquad()
                        .getStatusInfo(),
                equalTo(Response.Status.NOT_FOUND)
        );
    }


    private Response getSquad() {
        return request("/squad")
                .get();
    }

    public void store(Squad squad) {
        assertThat(
                request("/squad")
                        .post(Entity.json(squad))
                        .getStatusInfo(),
                equalTo(Response.Status.NO_CONTENT)
        );
    }

    public void assertStored(Squad squad) {
        Response response = getSquad();
        assertThat(response.getStatusInfo(), equalTo(Response.Status.OK));
        assertThat(response.getMediaType(), equalTo(MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.readEntity(Squad.class), equalTo(squad));
    }
}
