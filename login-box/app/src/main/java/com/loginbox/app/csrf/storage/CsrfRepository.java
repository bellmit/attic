package com.loginbox.app.csrf.storage;

import com.loginbox.app.csrf.CsrfToken;

/**
 * CSRF cookie storage interface. This should generally be backed by a database-mapping layer.
 */
public interface CsrfRepository {
    /**
     * Store a CSRF token to the database.
     * <p>
     * An example SQL implementation of this method:
     * <pre>
     *     insert into csrf_tokens
     *         (session, secret, issued_at)
     *     values
     *         (:sessionToken, :csrfToken.secret, now())
     * </pre>
     *
     * @param sessionToken
     *         the CSRF session secret.
     * @param csrfToken
     *         the CSRF token secret.
     */
    public void insertCsrfToken(String sessionToken, CsrfToken csrfToken);

    /**
     * Match and remove a CSRF token from the database, returning <code>true</code> if and only if a token was
     * consumed.
     * <p>
     * An example SQL implementation of this method:
     * <pre>
     *     delete from csrf_tokens
     *     where
     *         session = :sessionToken
     *         and secret = :csrfToken.secret
     * </pre>
     *
     * @param sessionToken
     *         the CSRF session secret to verify.
     * @param csrfToken
     *         the CSRF token secret to verify.
     * @return <code>true</code> if and only if the token has been matched and consumed.
     */
    public boolean consumeToken(String sessionToken, CsrfToken csrfToken);

    /**
     * Remove expired CSRF tokens from the database. This method also controls the duration of expiry.
     * <p>
     * An example SQL implementation of this method:
     * <pre>
     *     delete from csrf_tokens
     *     where now() - interval '5 minutes' > issued_at
     * </pre>
     */
    public void expireTokens();
}
