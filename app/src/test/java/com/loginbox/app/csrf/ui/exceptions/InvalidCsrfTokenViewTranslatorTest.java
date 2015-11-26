package com.loginbox.app.csrf.ui.exceptions;

import com.loginbox.app.csrf.context.InvalidCsrfTokenException;
import com.loginbox.app.csrf.ui.api.InvalidCsrfTokenResponse;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.loginbox.matchers.BadRequestMatcher.badRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class InvalidCsrfTokenViewTranslatorTest {
    private final InvalidCsrfTokenViewTranslator translator = new InvalidCsrfTokenViewTranslator();

    @Test
    public void convertsExceptionToView() {
        InvalidCsrfTokenException exception = new InvalidCsrfTokenException();
        Response response = translator.toResponse(exception);

        assertThat(response, is(badRequest()));
        assertThat(response.getEntity(), is(instanceOf(InvalidCsrfTokenResponse.class)));
    }
}