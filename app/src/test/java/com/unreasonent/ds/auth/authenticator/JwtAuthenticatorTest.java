package com.unreasonent.ds.auth.authenticator;

import com.auth0.jwt.JWTVerifier;
import com.google.common.base.Optional;
import com.unreasonent.ds.auth.User;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JwtAuthenticatorTest {
    private static final String SECRET = "secret";
    private final JWTVerifier verifier = new JWTVerifier(SECRET);

    // { sub: "VALID_TOKEN-subject" }
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJWQUxJRF9UT0tFTi1zdWJqZWN0In0.jiVs52mHLldaZbUvl3EWESKMR0UkEUnNX-fFzrjgwt4";

    // Same token, but with secret 'banana'
    private static final String WRONG_SECRET_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJWQUxJRF9UT0tFTi1zdWJqZWN0In0.ErBV2fcaqxsdk3w7JCfYNF1uWWjBFzfmj9VAaGls72U";

    // Expires at epoch second 100
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJFWFBJUkVEX1RPS0VOLXN1YmplY3QiLCJleHAiOjEwMH0.zDTikngMyzRgNczfnzndGwrWzY1NVN5P719NVr5axpM";

    // Not even jwt
    private static final String TOTAL_GARBAGE = "total fucking garbage";

    private final JwtAuthenticator authenticator = new JwtAuthenticator(verifier);

    @Test
    public void acceptsValidTokens() throws Exception {
        Optional<User> user = authenticator.authenticate(VALID_TOKEN);

        assertThat(user.isPresent(), equalTo(true));
        assertThat(user.get().getUserId(), equalTo("VALID_TOKEN-subject"));
    }

    @Test
    public void rejectsWrongSecretTokensSilently() throws Exception {
        Optional<User> user = authenticator.authenticate(WRONG_SECRET_TOKEN);

        assertThat(user.isPresent(), equalTo(false));
    }

    @Test
    public void rejectsExpiredTokensSilently() throws Exception {
        Optional<User> user = authenticator.authenticate(EXPIRED_TOKEN);

        assertThat(user.isPresent(), equalTo(false));
    }
    @Test
    public void rejectsTotalGarbageSilently() throws Exception {
        Optional<User> user = authenticator.authenticate(TOTAL_GARBAGE);

        assertThat(user.isPresent(), equalTo(false));
    }
}