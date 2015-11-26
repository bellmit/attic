package com.loginbox.app.csrf;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CsrfTokenTest {
    @Test
    public void supportsEquality() {
        CsrfToken a = new CsrfToken("abcd");
        CsrfToken b = new CsrfToken("abcd");

        assertThat(a.equals(b), is(true));
        assertThat(b.equals(a), is(true));
        assertThat(a.hashCode(), is(equalTo(b.hashCode())));
    }

    @Test
    public void supportsInequality() {
        CsrfToken a = new CsrfToken("abcd");
        CsrfToken b = new CsrfToken("wzyz");

        assertThat(a.equals(b), is(false));
        assertThat(b.equals(a), is(false));
    }

    @Test
    public void testGenerate() throws Exception {
        CsrfToken generated = CsrfToken.generate(() -> "abyz");

        assertThat(generated.getSecret(), is(equalTo("abyz")));
    }
}