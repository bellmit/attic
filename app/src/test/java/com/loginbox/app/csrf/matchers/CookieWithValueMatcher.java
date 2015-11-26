package com.loginbox.app.csrf.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.servlet.http.Cookie;
import java.util.Objects;

public class CookieWithValueMatcher extends TypeSafeDiagnosingMatcher<Cookie> {
    @Factory
    public static CookieWithValueMatcher cookieWithValue(String value) {
        return new CookieWithValueMatcher(value);
    }

    private final String value;

    public CookieWithValueMatcher(String value) {
        this.value = value;
    }

    @Override
    protected boolean matchesSafely(Cookie item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription
                    .appendText("cookie was null");
            return false;
        }

        if (!Objects.equals(value, item.getValue())) {
            mismatchDescription
                    .appendText("cookie with value ")
                    .appendValue(item.getValue());
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("cookie with value ")
                .appendValue(value);
    }
}
