package com.loginbox.app.views;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Provides site-wide conventions for "error" views, including standardized properties.
 * <p>
 * An {@link ErrorConvention} view can be constructed from an exception, and will have the following properties supplied
 * automatically: <ul> <li>{@code code} - a stable, machine-recognizable label for the error. This will normally be the
 * exception class name.</li> <li>{@code message} - a human-readable description of the error. This will normally be the
 * exception's message.</li> </ul>
 */
public class ErrorConvention extends ViewConvention {
    private final String code;
    private final String message;

    public ErrorConvention(Throwable cause, String view) {
        super(view);
        this.code = cause.getClass().getCanonicalName();
        this.message = cause.getMessage();
    }

    public ErrorConvention(Throwable cause, String view, Links links) {
        super(view, links);
        this.code = cause.getClass().getCanonicalName();
        this.message = cause.getMessage();
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public String getCode() {
        return code;
    }
}
