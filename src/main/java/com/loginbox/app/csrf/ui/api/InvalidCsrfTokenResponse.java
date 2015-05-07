package com.loginbox.app.csrf.ui.api;

import com.loginbox.app.csrf.context.InvalidCsrfTokenException;
import com.loginbox.app.views.ErrorConvention;

public class InvalidCsrfTokenResponse extends ErrorConvention {
    public InvalidCsrfTokenResponse(InvalidCsrfTokenException cause) {
        super(cause, "invalid-csrf-token.ftl");
    }
}
