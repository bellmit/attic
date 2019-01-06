package com.unreasonent.ds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loginbox.heroku.config.HerokuConfiguration;
import com.loginbox.heroku.db.HerokuDataSourceFactory;
import com.unreasonent.ds.auth.config.JwtVerifierFactory;
import com.unreasonent.ds.cors.config.CrossOriginFilterFactory;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;

public class DistantShoreConfiguration extends HerokuConfiguration {
    @Valid
    private JwtVerifierFactory oauth = new JwtVerifierFactory();

    @Valid
    private CrossOriginFilterFactory cors = new CrossOriginFilterFactory();

    @Valid
    private DataSourceFactory database = new HerokuDataSourceFactory();

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

    @JsonProperty
    public DataSourceFactory getDatabase() {
        return database;
    }

    @JsonProperty
    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }
}
