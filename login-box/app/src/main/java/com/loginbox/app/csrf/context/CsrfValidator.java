package com.loginbox.app.csrf.context;

import com.loginbox.app.csrf.CsrfToken;
import com.loginbox.app.csrf.SecretGenerator;
import com.loginbox.app.csrf.storage.CsrfRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * An issuer and validator for {@link com.loginbox.app.csrf.CsrfToken}s. Tokens issued by this class will be bound to
 * the browser session they're issued for, and will only successfully validate for that session. This binding is
 * maintained by cookies; clients <em>must</em> support cookies to participate in CSRF-guarded conversations.
 */
public class CsrfValidator {
    public static class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bind(CsrfValidator.class).to(CsrfValidator.class).in(RequestScoped.class);
        }
    }

    /**
     * Configures HK2 to inject an instance of {@link com.loginbox.app.csrf.context.CsrfValidator}.
     */
    public static Binder binder() {
        return new Binder();
    }

    private final SecretGenerator secretGenerator;
    private final CsrfCookies cookieFactory;
    private final CsrfRepository repository;

    @Inject
    public CsrfValidator(
            CsrfCookies cookieFactory,
            CsrfRepository repository,
            SecretGenerator secretGenerator) {
        this.secretGenerator = secretGenerator;
        this.cookieFactory = cookieFactory;
        this.repository = repository;
    }

    /**
     * Issues a new token for the requestor's browser session. This token will be valid once, and can be validated (and
     * consumed) by passing it or any equal token to {@link #consume(com.loginbox.app.csrf.CsrfToken)}. <p> If
     * necessary, this will also inject a new cookie into the current request, to tie the issued token to a browser
     * session. </p>
     *
     * @return a generated token.
     */
    public CsrfToken issue() {
        String csrfSession = ensureSession();
        CsrfToken csrfToken = CsrfToken.generate(secretGenerator);

        repository.insertCsrfToken(csrfSession, csrfToken);

        return csrfToken;
    }

    /**
     * Validates and consumes a token for the requestor's browser session. If the token is valid, it will be consumed
     * (and will not be valid on future requests). Otherwise, this will raise a {@link InvalidCsrfTokenException}. This
     * exception is itself a valid response.
     *
     * @param csrfToken
     *         the token to consume. As a convenience, callers may pass <code>null</code>, which is never valid.
     */
    public void consume(@Nullable CsrfToken csrfToken) {
        if (csrfToken == null)
            throw new InvalidCsrfTokenException();

        @Nullable
        String sessionToken = checkSession();
        if (sessionToken == null)
            throw new InvalidCsrfTokenException();

        repository.expireTokens();
        boolean consumed = repository.consumeToken(sessionToken, csrfToken);
        if (!consumed)
            throw new InvalidCsrfTokenException();
    }

    @Nullable
    private String checkSession() {
        @Nullable
        String sessionSecret = cookieFactory.readCookie();
        if (sessionSecret != null)
            return sessionSecret;
        return null;
    }

    private String ensureSession() {
        String existingSessionSecret = checkSession();
        if (existingSessionSecret != null)
            return existingSessionSecret;

        return cookieFactory.createCookie(secretGenerator);
    }
}
