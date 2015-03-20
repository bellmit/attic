package com.loginbox.heroku.http;

import com.google.common.io.CharStreams;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HerokuConnectorFactoryTest {
    @Test
    public void defaultPort() throws IOException, InterruptedException {
        assertThat(ConnectorFactoryPortHarness.run(null), is(HerokuConnectorFactory.DEFAULT_PORT));
    }

    @Test
    public void customPort() throws IOException, InterruptedException {
        assertThat(ConnectorFactoryPortHarness.run("2000"), is(2000));
    }
}

// My kingdom for a setenv call in Java's stdlib.
class ConnectorFactoryPortHarness {
    public static int run(String port) throws IOException, InterruptedException {
        String classpath = System.getProperty("java.class.path");
        String javaHome = System.getProperty("java.home");
        String javaLauncher = Paths.get(javaHome, "bin", "java").toString();

        ProcessBuilder pb = new ProcessBuilder();

        pb.command(
                javaLauncher,
                "-classpath", classpath,
                ConnectorFactoryPortHarness.class.getCanonicalName()
        );

        if (port == null) {
            pb.environment().remove("PORT");
        } else {
            pb.environment().put("PORT", port);
        }

        pb
                .inheritIO()
                .redirectOutput(ProcessBuilder.Redirect.PIPE);

        Process process = pb.start();
        /* Shockingly long, but sometimes CircleCI bogs down. */
        if (process.waitFor(5, TimeUnit.SECONDS))
            // default platform encoding is actually correct here, shockingly.
            try (InputStreamReader r = new InputStreamReader(process.getInputStream())) {
                String output = CharStreams.toString(r).trim();
                return Integer.parseInt(output);
            }
        process.destroyForcibly();
        throw new IOException("Failed to determine port.");
    }

    public static void main(String... args) {
        HerokuConnectorFactory factory = new HerokuConnectorFactory();
        System.out.println(factory.getPort());
    }
}