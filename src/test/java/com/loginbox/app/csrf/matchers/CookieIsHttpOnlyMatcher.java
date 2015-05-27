package com.loginbox.app.csrf.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.servlet.http.Cookie;
import java.util.Objects;

public class CookieIsHttpOnlyMatcher extends TypeSafeDiagnosingMatcher<Cookie> {
    @Factory
    public static CookieIsHttpOnlyMatcher cookieIsHttpOnly() {
        return new CookieIsHttpOnlyMatcher();
    }

    @Override
    protected boolean matchesSafely(Cookie item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription
                    .appendText("cookie was null");
            return false;
        }

        if (!item.isHttpOnly()) {
            mismatchDescription
                    .appendText("cookie was not HttpOnly");
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("cookie is HttpOnly");
    }
}
