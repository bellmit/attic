package com.unreasonent.ds.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Principal;
import java.util.Map;

public class User implements Principal {
    private final String userId;
    private final Map<String, Object> claims;

    public User(String userId, Map<String, Object> claims) {
        this.userId = userId;
        this.claims = claims;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return getUserId();
    }

    @JsonProperty
    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("User[%s]", userId);
    }
}
