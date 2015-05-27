package com.loginbox.app.csrf.matchers;

import com.loginbox.app.csrf.context.CsrfCookies;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import java.util.Objects;

import static com.loginbox.app.csrf.matchers.CookieIsHttpOnlyMatcher.cookieIsHttpOnly;
import static com.loginbox.app.csrf.matchers.CookieIsSecureMatcher.cookieIsNotSecure;
import static com.loginbox.app.csrf.matchers.CookieIsSecureMatcher.cookieIsSecure;
import static com.loginbox.app.csrf.matchers.CookieNamedMatcher.cookieNamed;
import static com.loginbox.app.csrf.matchers.CookieWithValueMatcher.cookieWithValue;
import static com.loginbox.app.csrf.matchers.SessionCookieMatcher.sessionCookie;

public class CsrfCookieMatcher {
    @Factory
    public static Matcher<Cookie> csrfCookie(String secret, boolean secure) {
        return Matchers.allOf(
                sessionCookie(),
                cookieNamed(CsrfCookies.COOKIE_NAME),
                cookieWithValue(secret),
                cookieIsHttpOnly(),
                secure ? cookieIsSecure() : cookieIsNotSecure()
        );
    }
}
