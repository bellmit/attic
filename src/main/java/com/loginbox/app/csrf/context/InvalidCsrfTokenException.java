package com.loginbox.app.csrf.context;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Thrown if an invalid token is {@link com.loginbox.app.csrf.context.CsrfValidator#consume(com.loginbox.app.csrf.CsrfToken)
 * consumed}.
 */
@SuppressWarnings("serial")
public class InvalidCsrfTokenException extends WebApplicationException {

    public InvalidCsrfTokenException() {
        super(invalidCsrfTokenResponse());
    }

    private static Response invalidCsrfTokenResponse() {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .build();
    }

}
