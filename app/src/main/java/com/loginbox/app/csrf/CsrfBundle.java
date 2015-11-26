package com.loginbox.app.csrf;

import com.loginbox.app.csrf.context.CsrfCookies;
import com.loginbox.app.csrf.context.CsrfValidator;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Provides one-shot CSRF tokens to your app. When used correctly, this protects your app from requests that were not
 * issued with the user's intent by validating that incoming requests have a valid one-shot token that was recently
 * issued to them.
 * <p>
 * The underlying CSRF protocol operates as follows: <ul> <li>In preparation for an action, clients must visit a page
 * that will issue a generated {@link CsrfToken}. The issued token will be associated (via a {@link
 * com.loginbox.app.csrf.storage.CsrfRepository} with that client via a cookie value.</li> <li>To perform an action, the
 * client must include the CsrfToken in the request body, and the corresponding session secret in a cookie header.</li>
 * <li>If the token is valid for that session, the token is consumed and invalidated for future requests, and the
 * request is accepted. If the token is not valid, the request will be rejected with a {@link
 * com.loginbox.app.csrf.context.InvalidCsrfTokenException 400 Bad Request} response.</li> </ul> To apply this
 * protection to requests, applications need to issue tokens during preparatory requests (usually, GETs), and consume
 * tokens during actions (POSTs, PUTs, DELETEs, and so on). This bundle provides an injectable, request-scoped {@link
 * com.loginbox.app.csrf.context.CsrfValidator} to handle these steps.
 * <p>
 * To issue a token, inject a CsrfValidator into your resource method:
 * <pre>
 *    {@literal @}GET
 *     public SomeModel get({@literal @}Context CsrfValidator csrfValidator) {
 *         CsrfToken token = csrfValidator.issue();
 *         return new SomeModel(token);
 *     }
 * </pre>
 * The corresponding response will contain both a Set-Cookie header for the CSRF session secret:
 * <pre>
 *     Set-Cookie: csrfSecret=ABCD; Secure; HttpOnly
 * </pre>
 * And a request token in the body:
 * <pre>
 *     { …, "csrfToken": "1234"}
 * </pre>
 * <p>
 * To consume a token, inject another validator:
 * <pre>
 *    {@literal @}POST
 *     public void post(SomeModel request,{@literal @}Context CsrfValidator csrfValidator) {
 *        {@literal @}Nullable
 *         CsrfToken token = request.getCsrfToken();
 *         csrfValidator.consume(token);
 *
 *         database.deleteEverythingForever(); // or other app actions.
 *     }
 * </pre>
 * The corresponding request must include both the CSRF session secret in a Cookie header:
 * <pre>
 *     Cookie: csrfSecret=ABCD
 * </pre>
 * And a valid CSRF request token in the body:
 * <pre>
 *     { …, "csrfToken": "1234"}
 * </pre>
 * The {@link com.loginbox.app.csrf.context.CsrfValidator#consume(CsrfToken)} method will ensure that the session secret
 * and the request token are both present, that the request token was issued for the right session, and that the request
 * token has not previously been consumed. A request that doesn't meet all of these criteria will cause the validator to
 * raise an exception and abort the request before the application has taken action.
 * <p>
 * Session secrets for CSRF are not to be reused for other purposes. In particular, they are not an authenticator. To
 * this end, the CSRF session secret is always flagged as HttpOnly, and will be flagged as Secure if the issuing request
 * was secure.
 * <p>
 * This must be paired with a bundle that provides dependency injection for {@link
 * com.loginbox.app.csrf.storage.CsrfRepository}s.
 */
public class CsrfBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(SecretGenerator.binder(SecretGenerator.uuidGenerator()));
        environment.jersey().register(CsrfCookies.binder());
        environment.jersey().register(CsrfValidator.binder());
    }
}
