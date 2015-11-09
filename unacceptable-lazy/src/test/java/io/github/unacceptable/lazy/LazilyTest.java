package io.github.unacceptable.lazy;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LazilyTest {
    @Test
    public void withExisting() {
        String value = Lazily.create("known", this::failOnGet);

        assertThat(value, equalTo("known"));
    }

    @Test
    public void newValue() {
        String value = Lazily.create(null, () -> "new value");

        assertThat(value, equalTo("new value"));
    }

    private String failOnGet() {
        throw new RuntimeException("supplier should not be invoked");
    }
}