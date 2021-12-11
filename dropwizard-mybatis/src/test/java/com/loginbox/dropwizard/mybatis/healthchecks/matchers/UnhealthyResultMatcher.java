package com.loginbox.dropwizard.mybatis.healthchecks.matchers;

import com.codahale.metrics.health.HealthCheck;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Objects;

public class UnhealthyResultMatcher extends TypeSafeDiagnosingMatcher<HealthCheck.Result> {
    private final Throwable expectedError;

    public UnhealthyResultMatcher(Throwable expectedError) {
        this.expectedError = expectedError;
    }

    @Factory
    public static UnhealthyResultMatcher unhealthyResult(Throwable expectedError) {
        return new UnhealthyResultMatcher(expectedError);
    }

    @Override
    protected boolean matchesSafely(HealthCheck.Result item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription
                    .appendText("null");
            return false;
        }

        if (item.isHealthy()) {
            mismatchDescription
                    .appendText("healthy result: ")
                    .appendValue(item);
            return false;
        }

        if (!Objects.equals(expectedError, item.getError())) {
            mismatchDescription
                    .appendText("unhealthy result with error: ")
                    .appendValue(item.getError());
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("unhealthy result with error: ")
                .appendValue(expectedError);
    }
}
