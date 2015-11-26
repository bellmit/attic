package com.loginbox.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import javax.ws.rs.core.Response;

public class BadRequestMatcher extends TypeSafeMatcher<Response> {
    private static final int BAD_REQUEST_STATUS = Response.Status.BAD_REQUEST.getStatusCode();

    @Factory
    public static BadRequestMatcher badRequest() {
        return new BadRequestMatcher();
    }

    public BadRequestMatcher() {
    }

    @Override
    protected boolean matchesSafely(Response item) {
        if (item == null)
            return false;
        if (item.getStatus() != BAD_REQUEST_STATUS)
            return false;

        return true;
    }

    @Override
    protected void describeMismatchSafely(Response item, Description mismatchDescription) {
        if (item == null)
            mismatchDescription.appendValue(null);
        else if (item.getStatus() != BAD_REQUEST_STATUS)
            mismatchDescription.appendText("status ")
                    .appendValue(item.getStatus())
                    .appendText(" response");
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("status ")
                .appendValue(BAD_REQUEST_STATUS)
                .appendText(" response");
    }
}
