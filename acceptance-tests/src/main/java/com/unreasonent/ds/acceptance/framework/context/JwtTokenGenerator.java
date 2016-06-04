package com.unreasonent.ds.acceptance.framework.context;

import com.auth0.jwt.JWTSigner;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtTokenGenerator implements Function<String, String> {
    public static final String DEFAULT_SECRET_BASE64 = "c2VjcmV0";

    private final JWTSigner signer;

    public JwtTokenGenerator() {
        this(DEFAULT_SECRET_BASE64);
    }

    public JwtTokenGenerator(String encodedSecret) {
        byte[] secret = Base64.getUrlDecoder().decode(encodedSecret);
        this.signer = new JWTSigner(secret);
    }

    @Override
    public String apply(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);

        return signer.sign(claims);
    }
}
