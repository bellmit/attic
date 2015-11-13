package io.github.unacceptable.dsl;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

public class SimpleDslTest {
    @Test
    public void createsDrivers() {
        Object driver = new Object();
        Object context = new Object();
        SimpleDsl<Object, Object> dsl = new SimpleDsl<>(() -> driver, context);

        assertThat(dsl.driver(), sameInstance(driver));
    }

    @Test
    public void hasContext() {
        Object context = new Object();
        SimpleDsl<Object, Object> dsl = new SimpleDsl<>(this::failIfInvoked, context);

        assertThat(dsl.testContext, sameInstance(context));
    }

    private Object failIfInvoked() {
        throw new RuntimeException("factory should not be invoked");
    }
}
