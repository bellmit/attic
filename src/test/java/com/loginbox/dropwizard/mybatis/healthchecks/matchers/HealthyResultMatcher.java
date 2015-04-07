package com.loginbox.dropwizard.mybatis.healthchecks.matchers;

import com.codahale.metrics.health.HealthCheck;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class HealthyResultMatcher extends TypeSafeDiagnosingMatcher<HealthCheck.Result> {
    @Factory
    public static HealthyResultMatcher healthyResult() {
        return new HealthyResultMatcher();
    }

    @Override
    protected boolean matchesSafely(HealthCheck.Result item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription
                    .appendText("null");
            return false;
        }

        if (!item.isHealthy()) {
            mismatchDescription
                    .appendText("unhealthy result: ")
                    .appendValue(item);
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a healthy result");
    }
}
