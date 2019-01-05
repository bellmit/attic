package com.loginbox.app.csrf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An externalizable representation of a CSRF prevention token. Tokens should generally be obtained from a {@link
 * com.loginbox.app.csrf.context.CsrfValidator#issue() CsrfValidator} injected into your resource; they can be validated
 * on incoming messages by passing the token back to {@link com.loginbox.app.csrf.context.CsrfValidator#consume(CsrfToken)}.
 * <p>
 * When embedded in a JSON message, a CSRF token will be flattened to a single string (its generated token value):
 * <pre>
 *     {
 *         "csrfToken": "0123456789abcdef0123456789abcdef"
 *     }
 * </pre>
 */
public class CsrfToken {
    private final String secret;

    /**
     * Given a generator, create a token.
     * <p>
     * This is nominally intended to be called with a method reference:
     * <pre>
     *     CsrfToken token = CsrfToken.generate(MyRandomGenerator::generate);
     * </pre>
     * <p>
     * The resistence of the returned token to analysis and brute force is only as good as the generator passed.
     *
     * @param secretGenerator
     *         a function returning a randomly-generated secret string.
     * @return a token whose value will be taken from the generator.
     */
    public static CsrfToken generate(SecretGenerator secretGenerator) {
        return new CsrfToken(secretGenerator.generate());
    }

    /**
     * Create a CSRF prevention token with a given token value. Generally, your application code should call {@link
     * com.loginbox.app.csrf.context.CsrfValidator#issue()}, instead. However, this method is used to parse incoming
     * tokens.
     *
     * @param secret
     *         the random token for this request.
     */
    @JsonCreator
    public CsrfToken(String secret) {
        this.secret = secret;
    }

    /**
     * Returns the CSRF prevention token's secret. This value must be safely delivered to the client, then validated
     * when the client sends the corresponding request.
     */
    @JsonValue
    public String getSecret() {
        return secret;
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
