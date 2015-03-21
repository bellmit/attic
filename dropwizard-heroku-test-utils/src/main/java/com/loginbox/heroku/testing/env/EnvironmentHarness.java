package com.loginbox.heroku.testing.env;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

/**
 * Creates a class in a subprocess, then serializes it using Jackson and recovers it in the current process.
 * <p>
 * This is mostly a workaround for the absence of {@code setenv} in Java: this provides tools for controlling the
 * environment in the child process, which means it can be used to test environment-sensitive components.
 */
public class EnvironmentHarness {
    @FunctionalInterface
    public interface EnvironmentKey {
        void applyTo(Map<String, String> environment);
    }

    public static EnvironmentKey unset(String name) {
        return (env) -> env.remove(name);
    }

    public static EnvironmentKey set(String name, String value) {
        return (env) -> env.put(name, value);
    }

    public static <T> T run(Class<T> subject, EnvironmentKey... environmentKeys) throws IOException, InterruptedException {
        String classpath = System.getProperty("java.class.path");
        String javaHome = System.getProperty("java.home");
        String javaLauncher = Paths.get(javaHome, "bin", "java").toString();

        ProcessBuilder pb = new ProcessBuilder();

        pb.command(
                javaLauncher,
                "-classpath", classpath,
                EnvironmentHarness.class.getCanonicalName(),
                subject.getCanonicalName()
        );

        Arrays
                .asList(environmentKeys)
                .forEach(key -> {
                    key.applyTo(pb.environment());
                });

        pb
                .inheritIO()
                .redirectOutput(ProcessBuilder.Redirect.PIPE);

        Process process = pb.start();
        try (InputStream jsonIn = process.getInputStream()) {
            ObjectMapper mapper = Jackson.newObjectMapper();
            return mapper.readValue(jsonIn, subject);
        } finally {
            process.destroyForcibly();
        }
    }

    public static void main(String... args) throws
            ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        Class<?> subject = Class.forName(args[0]);
        Object instance = subject.newInstance();

        ObjectMapper mapper = Jackson.newObjectMapper();
        mapper.writeValue(System.out, instance);
    }
}