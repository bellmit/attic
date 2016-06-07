package com.unreasonent.ds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loginbox.heroku.config.HerokuConfiguration;
import com.unreasonent.ds.auth.config.JwtVerifierFactory;
import com.unreasonent.ds.cors.config.CrossOriginFilterFactory;

import javax.validation.Valid;

public class DistantShoreConfiguration extends HerokuConfiguration {
    @Valid
    private JwtVerifierFactory oauth = new JwtVerifierFactory();

    @Valid
    private CrossOriginFilterFactory cors = new CrossOriginFilterFactory();

    @JsonProperty
    public JwtVerifierFactory getOauth() {
        return oauth;
    }

    @JsonProperty
    public void setOauth(JwtVerifierFactory oauth) {
        this.oauth = oauth;
    }

    @JsonProperty
    public CrossOriginFilterFactory getCors() {
        return cors;
    }

    @JsonProperty
    public void setCors(CrossOriginFilterFactory cors) {
        this.cors = cors;
    }
}
