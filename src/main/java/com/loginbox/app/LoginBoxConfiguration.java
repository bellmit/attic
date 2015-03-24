package com.loginbox.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loginbox.heroku.config.HerokuConfiguration;
import com.loginbox.heroku.db.HerokuDataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class LoginBoxConfiguration extends HerokuConfiguration {
    @Valid
    @NotNull
    private HerokuDataSourceFactory dataSourceFactory = new HerokuDataSourceFactory();

    @JsonProperty("database")
    public HerokuDataSourceFactory getDataSourceFactory() {
        return this.dataSourceFactory;
    }
}
