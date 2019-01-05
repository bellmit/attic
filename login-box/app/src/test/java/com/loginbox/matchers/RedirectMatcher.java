package com.loginbox.matchers;

import com.google.common.base.Predicates;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RedirectMatcher extends TypeSafeMatcher<Response> {
    private static final int REDIRECT_STATUS = Response.Status.SEE_OTHER.getStatusCode();

    @Factory
    public static RedirectMatcher redirectsTo(URI expectedLocation) {
        return new RedirectMatcher(expectedLocation);
    }

    private final List<String> expectedLocation;

    public RedirectMatcher(URI expectedLocation) {
        this.expectedLocation = Arrays.asList(expectedLocation.toString());
    }

    @Override
    protected boolean matchesSafely(Response item) {
        return Predicates.<Response>and(
                this::isRedirectStatus,
                this::isExpectedLocation
        ).apply(item);
    }

    private boolean isRedirectStatus(Response item) {
        return item.getStatus() == REDIRECT_STATUS;
    }

    private boolean isExpectedLocation(Response item) {
        List<String> locationHeader = locationHeader(item);
        return Objects.equals(expectedLocation, locationHeader);
    }

    private List<String> locationHeader(Response item) {
        MultivaluedMap<String, String> headers = item.getStringHeaders();
        return headers.get("Location");
    }

    @Override
    protected void describeMismatchSafely(Response item, Description mismatchDescription) {
        if (!isRedirectStatus(item))
            mismatchDescription.appendText("status ")
                    .appendValue(item.getStatus())
                    .appendValue("response ");

        if (!isExpectedLocation(item))
            mismatchDescription.appendText("redirecting to ")
                    .appendValueList("", ", ", "", locationHeader(item));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("redirect to ")
                .appendValue(expectedLocation);
    }
}
