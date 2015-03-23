package com.loginbox.heroku.http;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class HerokuServerFactoryTest {
    private final HerokuServerFactory factory = new HerokuServerFactory();

    @Test
    public void configuresConnectors() {
        assertThat(factory.getConnector(), is(instanceOf(HerokuConnectorFactory.class)));
    }

    @Test
    public void configuresContextPaths() {
        assertThat(factory.getApplicationContextPath(), is("/"));
        assertThat(factory.getAdminContextPath(), is("/!/admin"));
    }

    @Test
    public void configuresLogging() {
        assertThat(factory.getRequestLogFactory(), is(instanceOf(HerokuRequestLogFactory.class)));
    }
}