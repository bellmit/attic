package com.unreasonent.ds.auth.config;

import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Base64;

public class JwtVerifierFactory {
    @NotNull
    @NotEmpty
    private String secret = System.getenv("AUTH0_CLIENT_SECRET");

    @JsonProperty
    public String getSecret() {
        return secret;
    }

    @JsonProperty
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public JWTVerifier newVerifier() {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        byte[] secret = decoder.decode(this.secret);
        // TODO when https://github.com/auth0/java-jwt/issues/33 gets fixed, check issuer and audience.
        return new JWTVerifier(secret);
    }

}
