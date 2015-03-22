package com.loginbox.heroku.http;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class HerokuRequestLogFactoryTest {
    private final HerokuRequestLogFactory factory = new HerokuRequestLogFactory();

    @Test
    public void noAppenders() {
        assertThat(factory.getAppenders(), is(empty()));
    }
}