package io.github.unacceptable.dsl;

import io.github.unacceptable.lazy.Lazily;

import java.util.function.Supplier;

/**
 * Base class for DSL adapter layers based on a single driver. DSL adapters are expected to carry around a test context,
 * as well, which holds things like alias maps.
 *
 * @param <D>
 *         the driver type of this DSL adapter.
 * @param <T>
 *         the test context type.
 */
public class SimpleDsl<D, T> {
    private final Supplier<? extends D> driverFactory;
    private D driver = null;

    /**
     * The test context of the driver. Subclasses can look up aliases here and otherwise interact with the rest of the
     * test system.
     */
    protected final T testContext;

    public SimpleDsl(Supplier<? extends D> driverFactory, T testContext) {
        this.driverFactory = driverFactory;
        this.testContext = testContext;
    }

    /**
     * @return the underlying driver, creating it using the {@link #driverFactory} if necessariy.
     */
    protected D driver() {
        return driver = Lazily.create(driver, driverFactory);
    }
}
