package io.github.unacceptable.lazy;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LazilyTest {
    @Before
    public void setKnownProperties() {
        System.setProperty(
                "io.github.unacceptable.lazy.set",
                "property value");
        System.clearProperty("io.github.unacceptable.lazy.unset");
    }

    @Test
    public void createWithExisting() {
        String value = Lazily.create("known", this::failOnGet);

        assertThat(value, equalTo("known"));
    }

    @Test
    public void createNewValue() {
        String value = Lazily.create(null, () -> "new value");

        assertThat(value, equalTo("new value"));
    }

    @Test
    public void systemPropertyWithExisting() {
        String value = Lazily.systemProperty("known", "io.github.unacceptable.lazy.unset", this::failOnGet);

        assertThat(value, equalTo("known"));
    }

    @Test
    public void systemPropertyFromProperty() {
        String value = Lazily.systemProperty(null, "io.github.unacceptable.lazy.set", this::failOnGet);

        assertThat(value, equalTo("property value"));
    }

    @Test
    public void systemPropertyFromDefault() {
        String value = Lazily.systemProperty(null, "io.github.unacceptable.lazy.unset", () -> "new value");

        assertThat(value, equalTo("new value"));
    }

    private String failOnGet() {
        throw new RuntimeException("supplier should not be invoked");
    }
}