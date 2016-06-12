package com.unreasonent.ds.bundle;

import io.dropwizard.Bundle;
import io.dropwizard.ConfiguredBundle;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class WrapsBundle<T> extends TypeSafeDiagnosingMatcher<ConfiguredBundle<T>> {
    @Factory
    public static <T> WrapsBundle<T> wrapsBundle(Matcher<? extends Bundle> bundleMatcher) {
        return new WrapsBundle<>(bundleMatcher);
    }

    private final Matcher<? extends Bundle> bundleMatcher;

    public WrapsBundle(Matcher<? extends Bundle> bundleMatcher) {
        this.bundleMatcher = bundleMatcher;
    }

    @Override
    protected boolean matchesSafely(ConfiguredBundle<T> item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("was null");
            return false;
        }

        if (!(item instanceof BundleAdapter)) {
            mismatchDescription.appendText("was not a bundle adapter");
            return false;
        }

        BundleAdapter<?> bundleAdapter = (BundleAdapter) item;
        if (!bundleMatcher.matches(bundleAdapter.bundle)) {
            mismatchDescription.appendText("was a bundle adapter whose bundle ");
            bundleMatcher.describeMismatch(item, mismatchDescription);
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("is a bundle adapter whose bundle ");
        bundleMatcher.describeTo(description);
    }
}
