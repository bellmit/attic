package com.loginbox.app.csrf.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.servlet.http.Cookie;
import java.util.Objects;

public class CookieNamedMatcher extends TypeSafeDiagnosingMatcher<Cookie> {
    @Factory
    public static CookieNamedMatcher cookieNamed(String name) {
        return new CookieNamedMatcher(name);
    }

    private final String name;

    public CookieNamedMatcher(String name) {
        this.name = name;
    }

    @Override
    protected boolean matchesSafely(Cookie item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription
                    .appendText("cookie was null");
            return false;
        }

        if (!Objects.equals(name, item.getName())) {
            mismatchDescription
                    .appendText("cookie named ")
                    .appendValue(item.getName());
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("cookie named ")
                .appendValue(name);
    }
}
