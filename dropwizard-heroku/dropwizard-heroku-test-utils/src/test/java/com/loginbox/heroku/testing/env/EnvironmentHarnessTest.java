package com.loginbox.heroku.testing.env;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static com.loginbox.heroku.testing.env.EnvironmentHarness.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class EnvironmentHarnessTest {
    @Test
    public void setsEnvironmentKeys() {
        Map<String, String> env = Maps.newHashMap();

        set("FOO", "foo").applyTo(env);

        assertThat(env.get("FOO"), is("foo"));
    }

    @Test
    public void unsetClearsEnvironmentKeys() {
        Map<String, String> env = Maps.newHashMap();
        env.put("FOO", "foo");

        unset("FOO").applyTo(env);

        assertThat(env.get("FOO"), is(nullValue()));
    }

    @Test
    public void runAppliesEnvironmentAndObtainsResult() throws IOException, InterruptedException {
        TestSubject result = run(TestSubject.class, set("FOO", "one"));
        assertThat(result.getValue(), is("one"));
    }
}

class TestSubject {
    private final String value;

    public TestSubject() {
        this.value = System.getenv("FOO");
    }

    @JsonProperty
    public String getValue() {
        return value;
    }
}