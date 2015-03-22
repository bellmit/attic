package com.example.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loginbox.heroku.config.HerokuConfiguration;
import com.loginbox.heroku.db.HerokuDataSourceFactory;

public class MigrationsAppConfiguration extends HerokuConfiguration {
    private HerokuDataSourceFactory dataSourceFactory = new HerokuDataSourceFactory();

    @JsonProperty("database")
    public HerokuDataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }
}
