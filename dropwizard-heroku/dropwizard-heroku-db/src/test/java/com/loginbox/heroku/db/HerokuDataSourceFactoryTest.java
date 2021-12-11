package com.loginbox.heroku.db;

import org.junit.Test;

import java.io.IOException;

import static com.loginbox.heroku.testing.env.EnvironmentHarness.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class HerokuDataSourceFactoryTest {
    @Test
    public void parsesDatabaseUrl() throws IOException, InterruptedException {
        HerokuDataSourceFactory f = run(
                HerokuDataSourceFactory.class,
                // Trailing %26 is an escaped & to verify that escaped characters are handled sanely.
                // Spoiler alert: they aren't. Thanks, java.net.URI!
                set("DATABASE_URL", "postgres://user:pass@db.example.com/database?query=%26")
        );
        assertThat(f.getDriverClass(), equalTo("org.postgresql.Driver"));
        assertThat(f.getUrl(), equalTo("jdbc:postgresql://db.example.com/database?query=&"));
        assertThat(f.getUser(), equalTo("user"));
        assertThat(f.getPassword(), equalTo("pass"));
    }

    @Test
    public void permitsMissingCredentials() throws IOException, InterruptedException {
        HerokuDataSourceFactory f = run(
                HerokuDataSourceFactory.class,
                set("DATABASE_URL", "postgres://db.example.com/database")
        );
        assertThat(f.getDriverClass(), equalTo("org.postgresql.Driver"));
        assertThat(f.getUrl(), equalTo("jdbc:postgresql://db.example.com/database"));
        assertThat(f.getUser(), nullValue());
        assertThat(f.getPassword(), nullValue());
    }

    @Test
    public void noEnvironmentNoConfig() throws IOException, InterruptedException {
        HerokuDataSourceFactory f = run(
                HerokuDataSourceFactory.class,
                unset("DATABASE_URL")
        );
        assertThat(f.getDriverClass(), equalTo("org.postgresql.Driver"));
        assertThat(f.getUrl(), nullValue());
        assertThat(f.getUser(),nullValue());
        assertThat(f.getPassword(), nullValue());
    }
}