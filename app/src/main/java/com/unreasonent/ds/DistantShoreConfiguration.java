package com.unreasonent.ds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loginbox.heroku.config.HerokuConfiguration;
import com.unreasonent.ds.auth.config.JwtVerifierFactory;

import javax.validation.Valid;

public class DistantShoreConfiguration extends HerokuConfiguration {
    @Valid
    private JwtVerifierFactory oauth = new JwtVerifierFactory();

    @JsonProperty
    public JwtVerifierFactory getOauth() {
        return oauth;
    }

    @JsonProperty
    public void setOauth(JwtVerifierFactory oauth) {
        this.oauth = oauth;
    }
}
