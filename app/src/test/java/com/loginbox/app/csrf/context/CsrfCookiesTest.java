package com.loginbox.app.csrf.context;

import com.loginbox.app.csrf.SecretGenerator;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.loginbox.app.csrf.matchers.CsrfCookieMatcher.csrfCookie;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsrfCookiesTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final CsrfCookies csrfCookies = new CsrfCookies(request, response);

    private final String generatedSecret = "GENERATED-SECRET";
    private final SecretGenerator secretGenerator = () -> generatedSecret;

    @Test
    public void readsCookiesWhenPresent() {
        when(request.getCookies()).thenReturn(new Cookie[]{
                new Cookie(CsrfCookies.COOKIE_NAME, "abcd")
        });

        assertThat(csrfCookies.readCookie(), is("abcd"));
    }

    @Test
    public void readsCookiesWhenAbsent() {
        when(request.getCookies()).thenReturn(new Cookie[]{
        });

        assertThat(csrfCookies.readCookie(), is(nullValue()));
    }

    @Test
    public void readsCookiesWhenNoCookies() {
        when(request.getCookies()).thenReturn(null);

        assertThat(csrfCookies.readCookie(), is(nullValue()));
    }

    @Test
    public void storesNewCookiePlainHttp() {
        /* Sure, it's a terrible idea. It should still _work._ */
        when(request.isSecure()).thenReturn(false);
        String secret = csrfCookies.createCookie(secretGenerator);

        assertThat(secret, is(equalTo(generatedSecret)));
        verify(response).addCookie(argThat(is(csrfCookie(generatedSecret, false))));
    }

    @Test
    public void storesNewCookiePlainHttps() {
        when(request.isSecure()).thenReturn(true);
        String secret = csrfCookies.createCookie(secretGenerator);

        assertThat(secret, is(equalTo(generatedSecret)));
        verify(response).addCookie(argThat(is(csrfCookie(generatedSecret, true))));
    }
}