package io.github.unacceptable.dropwizard.context;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StringsTest {
    @Test
    public void stripLeadingSlashes() {
        assertThat(Strings.stripLeadingSlashes(""), equalTo(""));
        assertThat(Strings.stripLeadingSlashes("/"), equalTo(""));
        assertThat(Strings.stripLeadingSlashes("////"), equalTo(""));
        assertThat(Strings.stripLeadingSlashes("foo"), equalTo("foo"));
        assertThat(Strings.stripLeadingSlashes("/foo/"), equalTo("foo/"));
        assertThat(Strings.stripLeadingSlashes("/////foo/bar"), equalTo("foo/bar"));
    }

    @Test
    public void stripTrailingSlashes() {
        assertThat(Strings.stripTrailingSlashes(""), equalTo(""));
        assertThat(Strings.stripTrailingSlashes("/"), equalTo(""));
        assertThat(Strings.stripTrailingSlashes("////"), equalTo(""));
        assertThat(Strings.stripTrailingSlashes("foo"), equalTo("foo"));
        assertThat(Strings.stripTrailingSlashes("/foo/"), equalTo("/foo"));
        assertThat(Strings.stripTrailingSlashes("foo/bar/////"), equalTo("foo/bar"));
    }
}