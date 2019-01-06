package io.github.unacceptable.lazy;

import java.util.function.Supplier;

public class Lazily {
    private Lazily() {
        throw new UnsupportedOperationException();
    }

    /**
     * Lazy-init helper. Returns the first argument if non-null, or invokes the second argument's {@link Supplier#get()}
     * method and returns the result otherwise. <p> Intended usage pattern: </p>
     * <pre>
     *     public Config config() {
     *         return this.config = Lazily.create(this.config, this::loadConfigFromFile);
     *     }
     * </pre>
     * <p> You can also pass a lambda or a pre-existing {@link Supplier} instead of a method handle. </p>
     *
     * @param value
     *         the initializable field's current value.
     * @param initialiser
     *         a factory for creating a new value.
     * @param <T>
     *         the type of the objects being lazily created.
     * @return either <var>value</var> or a new object from <var>initialiser</var>.
     */
    public static <T> T create(T value, Supplier<? extends T> initialiser) {
        if (value != null)
            return value;
        return initialiser.get();
    }

    /**
     * Lazy-init helper for values obtained from {@link System#getProperty(String, String) system properties}. If the
     * first argument is non-null, it will be returned as is. Otherwise, if the named property is set, its value will be
     * returned. Otherwise, the value of the default callback will be returned. <p> The default callback is guaranteed
     * not to be invoked unless its value is needed. <p> The intended usage pattern:
     * <pre>
     *     public String filename() {
     *         return this.filename = Lazily.systemProperty(this.filename, "file.name", this::defaultFilename);
     *     }
     * </pre>
     * <p> You can also pass a lambda or a pre-existing {@link Supplier} instead of a method handle. </p>
     *
     * @param value
     *         the initializable field's current value.
     * @param property
     *         the name of the system property to read.
     * @param defaultCallback
     *         a factory for creating the default value if neither the current value nor a system property are set.
     * @return either <var>value</var>, or the value of the named system property, or the result of invoking the default
     * callback.
     */
    public static String systemProperty(String value, String property, Supplier<? extends String> defaultCallback) {
        if (value != null)
            return value;
        value = System.getProperty(property);
        if (value != null)
            return value;
        return defaultCallback.get();
    }
}
