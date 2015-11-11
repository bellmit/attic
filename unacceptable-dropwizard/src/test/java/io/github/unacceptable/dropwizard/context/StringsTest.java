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

    @Test
    public void findLastNonSlash() {
        assertThat(Strings.findLastNonSlash(""), equalTo(0));
        assertThat(Strings.findLastNonSlash("/"), equalTo(0));
        assertThat(Strings.findLastNonSlash("////"), equalTo(0));
        assertThat(Strings.findLastNonSlash("foo"), equalTo(3));
        assertThat(Strings.findLastNonSlash("/foo/"), equalTo(4));
        assertThat(Strings.findLastNonSlash("foo/bar/////"), equalTo(7));

    }

    @Test
    public void findFirstNonSlash() {
        assertThat(Strings.findFirstNonSlash(""), equalTo(0));
        assertThat(Strings.findFirstNonSlash("/"), equalTo(1));
        assertThat(Strings.findFirstNonSlash("////"), equalTo(4));
        assertThat(Strings.findFirstNonSlash("foo"), equalTo(0));
        assertThat(Strings.findFirstNonSlash("/foo/"), equalTo(1));
        assertThat(Strings.findFirstNonSlash("foo/bar/////"), equalTo(0));
    }
}