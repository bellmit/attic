package com.unreasonent.ds.auth.config;

import com.auth0.jwt.JWTVerifier;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JwtVerifierFactoryTest {
    // Constants and test data cribbed from https://jwt.io
    // URL-safe Base64 of 'secret'
    private static final String KNOWN_GOOD_SECRET = "c2VjcmV0";
    private static final String KNOWN_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";
    private static final Map<String, Object> KNOWN_CLAIMS = new HashMap<String, Object>() {
        private static final long serialVersionUID = -1;

        {
            put("sub", "1234567890");
            put("name", "John Doe");
            put("admin", true);
        }
    };

    @Test
    public void createsKnownGoodVerifier() throws Exception {
        JwtVerifierFactory factory = new JwtVerifierFactory();
        factory.setSecret(KNOWN_GOOD_SECRET);

        JWTVerifier verifier = factory.newVerifier();

        assertThat(verifier.verify(KNOWN_TOKEN), equalTo(KNOWN_CLAIMS));
    }
}