package com.loginbox.app.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loginbox.app.csrf.CsrfToken;

import javax.annotation.Nullable;

public class FormConvention extends ViewConvention {
    @Nullable
    private CsrfToken csrfToken;

    protected FormConvention(CsrfToken csrfToken, String templateName) {
        super(templateName);
        this.csrfToken = csrfToken;
    }

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public CsrfToken getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(@Nullable CsrfToken csrfToken) {
        this.csrfToken = csrfToken;
    }
}
