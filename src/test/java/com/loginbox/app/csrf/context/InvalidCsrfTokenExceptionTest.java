package com.loginbox.app.csrf.context;

import org.junit.Test;

import static com.loginbox.matchers.BadRequestMatcher.badRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InvalidCsrfTokenExceptionTest {
    @Test
    public void is400Response() {
        InvalidCsrfTokenException exception = new InvalidCsrfTokenException();

        assertThat(exception.getResponse(), is(badRequest()));
    }
}