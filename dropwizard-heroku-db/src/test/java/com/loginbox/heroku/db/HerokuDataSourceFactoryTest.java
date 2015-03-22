package com.loginbox.heroku.db;

import org.junit.Test;

import java.io.IOException;

import static com.loginbox.heroku.testing.env.EnvironmentHarness.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class HerokuDataSourceFactoryTest {
    @Test
    public void parsesDatabaseUrl() throws IOException, InterruptedException {
        HerokuDataSourceFactory f = run(
                HerokuDataSourceFactory.class,
                set("DATABASE_URL", "postgres://user:pass@db.example.com/database")
        );
        assertThat(f.getDriverClass(), is("org.postgresql.Driver"));
        assertThat(f.getUrl(), is("jdbc:postgresql://db.example.com/database"));
        assertThat(f.getUser(), is("user"));
        assertThat(f.getPassword(), is("pass"));
    }

    @Test
    public void permitsMissingCredentials() throws IOException, InterruptedException {
        HerokuDataSourceFactory f = run(
                HerokuDataSourceFactory.class,
                set("DATABASE_URL", "postgres://db.example.com/database")
        );
        assertThat(f.getDriverClass(), is("org.postgresql.Driver"));
        assertThat(f.getUrl(), is("jdbc:postgresql://db.example.com/database"));
        assertThat(f.getUser(), is(nullValue()));
        assertThat(f.getPassword(), is(""));
    }

    @Test
    public void noEnvironmentNoConfig() throws IOException, InterruptedException {
        HerokuDataSourceFactory f = run(
                HerokuDataSourceFactory.class,
                unset("DATABASE_URL")
        );
        assertThat(f.getDriverClass(), is("org.postgresql.Driver"));
        assertThat(f.getUrl(), is(nullValue()));
        assertThat(f.getUser(), is(nullValue()));
        assertThat(f.getPassword(), is("")); // surprise!
    }
}