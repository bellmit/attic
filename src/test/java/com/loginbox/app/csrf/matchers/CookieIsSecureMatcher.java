package com.loginbox.app.csrf.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.servlet.http.Cookie;

public class CookieIsSecureMatcher extends TypeSafeDiagnosingMatcher<Cookie> {
    @Factory
    public static CookieIsSecureMatcher cookieIsSecure() {
        return new CookieIsSecureMatcher(true);
    }

    @Factory
    public static CookieIsSecureMatcher cookieIsNotSecure() {
        return new CookieIsSecureMatcher(false);
    }

    private final boolean secure;

    public CookieIsSecureMatcher(boolean secure) {
        this.secure = secure;
    }

    @Override
    protected boolean matchesSafely(Cookie item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription
                    .appendText("cookie was null");
            return false;
        }

        if (item.getSecure() && !secure) {
            mismatchDescription
                    .appendText("cookie was secure");
            return false;
        }

        if (!item.getSecure() && secure) {
            mismatchDescription
                    .appendText("cookie was not secure");
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        if (secure)
            description
                    .appendText("cookie is secure");
        else
            description
                    .appendText("cookie is not secure");
    }
}
