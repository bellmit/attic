package com.loginbox.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.ws.rs.core.Response;
import java.util.Objects;

public class HasHttpStatusMatcher extends TypeSafeDiagnosingMatcher<Response> {
    private final Response.StatusType status;

    @Factory
    public static HasHttpStatusMatcher hasHttpStatus(Response.StatusType status) {
        return new HasHttpStatusMatcher(status);
    }

    public HasHttpStatusMatcher(Response.StatusType status) {
        this.status = status;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("status ")
                .appendValue(status)
                .appendText(" response");
    }

    @Override
    protected boolean matchesSafely(Response item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendValue(null);
            return false;
        }

        if (!Objects.equals(status, item.getStatusInfo())) {
            mismatchDescription.appendText("status ")
                    .appendValue(item.getStatusInfo())
                    .appendText(" response");
            return false;
        }

        return true;
    }
}
