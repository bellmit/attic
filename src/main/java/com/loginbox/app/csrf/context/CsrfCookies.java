package com.loginbox.app.csrf.context;

import com.loginbox.app.csrf.SecretGenerator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

/**
 * Manipulates request cookies used to bind CSRF secrets to browser sessions. Under the hood, this wraps an
 * HttpServletRequest/HttpServletResponse pair, and uses them to hand session secrets off to the browser for
 * safekeeping.
 */
public class CsrfCookies {
    public static class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(CsrfCookies.class).to(CsrfCookies.class).in(RequestScoped.class);
        }
    }

    /**
     * Configures HK2 to inject an instance of {@link com.loginbox.app.csrf.context.CsrfCookies} for each request.
     */
    public static Binder binder() {
        return new Binder();
    }

    /**
     * The name of the CSRF secret cookie.
     */
    public static final String COOKIE_NAME = "csrfSecret";

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Logger logger = LoggerFactory.getLogger(CsrfCookies.class);

    @Inject
    public CsrfCookies(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * Sends a new cookie for a given session secret. The new cookie will _always_ be marked <code>HttpOnly</code>, and
     * will be marked <code>Secure</code> if the request claims to be secure.
     *
     * @param secretGenerator
     *         a secret generator to consult if a new token is needed.
     */
    public String createCookie(SecretGenerator secretGenerator) {
        String secret = secretGenerator.generate();
        Cookie cookie = new Cookie(COOKIE_NAME, secret);

        boolean secure = request.isSecure();
        if (!secure)
            logger.warn("Storing CSRF session secret in a non-secure cookie. This is unavoidably vulnerable to FireSheep-style capture. Deploy your application over HTTPS to suppress this warning.");
        cookie.setSecure(secure);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return secret;
    }

    /**
     * Returns the CSRF session secret for a request.
     *
     * @return the current request's CSRF session secret, or <code>null</code> if the request has no CSRF session
     * secret.
     */
    @Nullable
    String readCookie() {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;

        return Arrays.asList(cookies)
                .stream()
                .filter(this::isCsrfSecret)
                .map(this::extractCsrfSecret)
                .findFirst()
                .orElse(null);
    }

    private String extractCsrfSecret(Cookie cookie) {
        return cookie.getValue();
    }

    private boolean isCsrfSecret(Cookie cookie) {
        return Objects.equals(COOKIE_NAME, cookie.getName());
    }
}
