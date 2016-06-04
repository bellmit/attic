package com.unreasonent.ds.auth.authenticator;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.google.common.base.Optional;
import com.unreasonent.ds.auth.User;
import io.dropwizard.auth.AuthenticationException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

public class JwtAuthenticator implements io.dropwizard.auth.Authenticator<String, User> {
    private static final String SUBJECT = "sub";

    private final JWTVerifier verifier;

    public JwtAuthenticator(JWTVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public Optional<User> authenticate(String credentials) throws AuthenticationException {
        try {
            Map<String, Object> claims = verifier.verify(credentials);
            Object subject = claims.get(SUBJECT);
            if (subject != null) {
                User user = new User(subject.toString(), claims);
                return Optional.of(user);
            }
            return Optional.absent();
        } catch (NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            throw new AuthenticationException(e);
        } catch (IllegalStateException | SignatureException | JWTVerifyException e) {
            return Optional.absent();
        }
    }
}
