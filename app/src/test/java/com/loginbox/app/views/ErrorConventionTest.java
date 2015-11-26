package com.loginbox.app.views;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ErrorConventionTest {
    @Test
    public void extractsMessage() {
        Exception cause = new Exception("a message");

        ErrorConvention view = new ErrorConvention(cause, "an-error.ftl");

        assertThat(view.getMessage(), is("a message"));
    }

    @Test
    public void extractsCode() {
        Exception cause = new Exception("a message");

        ErrorConvention view = new ErrorConvention(cause, "an-error.ftl");

        assertThat(view.getCode(), is("java.lang.Exception"));
    }
}