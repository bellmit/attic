package com.loginbox.app.acceptance.framework.driver;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class PublicApiDriver {
    private static final String VERSION_HEADER = "X-Version";

    private final SystemDriver systemDriver;
    private final String rootUrl;

    public PublicApiDriver(SystemDriver systemDriver, String rootUrl) {
        this.systemDriver = systemDriver;
        this.rootUrl = rootUrl;
    }

    public String getVersion() {
        Response response = target().request().head();
        try {
            return response.getHeaderString(VERSION_HEADER);
        } finally {
            response.close();
        }
    }

    private WebTarget target() {
        return client().target(rootUrl);
    }

    private Client client() {
        return systemDriver.publicApiClient();
    }
}
