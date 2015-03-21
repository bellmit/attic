package com.loginbox.heroku.http;

import org.junit.Test;

import java.io.IOException;

import static com.loginbox.heroku.testing.env.EnvironmentHarness.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HerokuConnectorFactoryTest {
    @Test
    public void defaultPort() throws IOException, InterruptedException {
        HerokuConnectorFactory factory = run(HerokuConnectorFactory.class, unset("PORT"));
        assertThat(factory.getPort(), is(HerokuConnectorFactory.DEFAULT_PORT));
    }

    @Test
    public void customPort() throws IOException, InterruptedException {
        HerokuConnectorFactory factory = run(HerokuConnectorFactory.class, set("PORT", "2000"));
        assertThat(factory.getPort(), is(2000));
    }
}
