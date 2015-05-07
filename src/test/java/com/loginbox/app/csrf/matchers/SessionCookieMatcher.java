package com.loginbox.app.csrf.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.servlet.http.Cookie;

public class SessionCookieMatcher extends TypeSafeDiagnosingMatcher<Cookie> {
    @Factory
    public static SessionCookieMatcher sessionCookie() {
        return new SessionCookieMatcher();
    }

    @Override
    protected boolean matchesSafely(Cookie item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription
                    .appendText("cookie was null");
            return false;
        }

        if (item.getMaxAge() >= 0) {
            mismatchDescription
                    .appendText("cookie persists for ")
                    .appendValue(item.getMaxAge())
                    .appendText(" seconds");
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("cookie expires with browser session");
    }
}
