package com.loginbox.app.config;

import java.util.function.Function;

public class Environment {
    /**
     * Returns a config value from the environment, converting it to the required type.
     *
     * @param name
     *         the environment variable name to read.
     * @param converter
     *         a function that converts the string from the environment into the appropriate domain type.
     * @param defaultValue
     *         a default value to use if the environment variable is unset.
     * @param <T>
     *         the type of the config value to return.
     * @return a @{code T} from the environment, or the default value.
     */
    public static <T> T config(String name, Function<? super String, ? extends T> converter, T defaultValue) {
        String value = System.getenv(name);
        if (value == null)
            return defaultValue;
        return converter.apply(value);
    }
}
