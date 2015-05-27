package com.loginbox.app.csrf.context;

import com.loginbox.app.csrf.CsrfToken;
import com.loginbox.app.csrf.SecretGenerator;
import com.loginbox.app.csrf.storage.CsrfRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsrfValidatorTest {
    private final String generatedSecret = "GENERATED-SECRET";
    private final String generatedSessionToken = "GENERATED-SESSION-TOKEN";

    private final SecretGenerator secretGenerator = mock(SecretGenerator.class);
    private final CsrfCookies csrfCookies = mock(CsrfCookies.class);
    private final CsrfRepository repository = mock(CsrfRepository.class);
    private final CsrfValidator validator = new CsrfValidator(
            csrfCookies, repository, secretGenerator);

    @Before
    public void wireMocks() {
        when(secretGenerator.generate()).thenReturn(generatedSecret);
        when(csrfCookies.createCookie(secretGenerator)).thenReturn(generatedSessionToken);
    }

    @Test
    public void issuesTokensForSession() {
        when(csrfCookies.readCookie()).thenReturn(generatedSessionToken);

        CsrfToken csrfToken = validator.issue();

        assertThat(csrfToken.getSecret(), is(equalTo(generatedSecret)));

        verify(repository).insertCsrfToken(generatedSessionToken, csrfToken);
    }

    @Test
    public void consumesValidTokens() {
        CsrfToken token = new CsrfToken(generatedSecret);

        when(csrfCookies.readCookie()).thenReturn(generatedSessionToken);
        when(repository.consumeToken(generatedSessionToken, token)).thenReturn(true);

        validator.consume(token);

        InOrder consumeOrder = inOrder(repository);
        consumeOrder.verify(repository).expireTokens();
        consumeOrder.verify(repository).consumeToken(generatedSessionToken, token);
    }

    @Test
    public void nullIsInvalidToken() {
        try {
            validator.consume(null);
            fail();
        } catch (InvalidCsrfTokenException expected) {
            /* success */
        }
    }

    @Test
    public void rejectsNonNullTokensWhenNoSession() {
        CsrfToken token = new CsrfToken(generatedSecret);

        when(csrfCookies.readCookie()).thenReturn(null);

        try {
            validator.consume(token);
            fail();
        } catch (InvalidCsrfTokenException expected) {
            /* success */
        }
    }

    @Test
    public void rejectsInvalidNonNullTokens() {
        CsrfToken token = new CsrfToken(generatedSecret);

        when(csrfCookies.readCookie()).thenReturn(generatedSessionToken);
        when(repository.consumeToken(generatedSessionToken, token)).thenReturn(false);

        try {
            validator.consume(token);
            fail();
        } catch (InvalidCsrfTokenException expected) {
            /* success, but */
            InOrder consumeOrder = inOrder(repository);
            consumeOrder.verify(repository).expireTokens();
            consumeOrder.verify(repository).consumeToken(generatedSessionToken, token);
        }
    }
}
