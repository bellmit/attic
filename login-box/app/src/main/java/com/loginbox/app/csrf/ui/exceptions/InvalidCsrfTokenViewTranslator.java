package com.loginbox.app.csrf.ui.exceptions;

import com.loginbox.app.csrf.context.InvalidCsrfTokenException;
import com.loginbox.app.csrf.ui.api.InvalidCsrfTokenResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Generates a nicer UI when an invalid CSRF token is encountered.
 */
@Provider
public class InvalidCsrfTokenViewTranslator implements ExceptionMapper<InvalidCsrfTokenException> {

    @Override
    public Response toResponse(InvalidCsrfTokenException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new InvalidCsrfTokenResponse(exception))
                .build();
    }
}
