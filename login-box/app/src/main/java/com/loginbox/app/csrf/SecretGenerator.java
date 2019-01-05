package com.loginbox.app.csrf;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import java.util.UUID;

/**
 * Generates secure token values.
 * <p>
 * In theory, any String-generating function will do. However, the security properties of CSRF tokens require <ul>
 * <li>that tokens be unpredictable</li> <li>that tokens be long enough to make brute force searches impractical</li>
 * </ul>
 * <p>
 * In practice, applications should generally use a generator obtained from a static method on this class.
 */
@FunctionalInterface
public interface SecretGenerator {
    public static class Binder extends AbstractBinder {
        private final SecretGenerator generator;

        public Binder(SecretGenerator generator) {
            this.generator = generator;
        }

        @Override
        protected void configure() {
            bind(generator).to(SecretGenerator.class);
        }
    }

    public static Binder binder(SecretGenerator generator) {
        return new Binder(generator);
    }

    /**
     * Returns a TokenGenerator backed by UUID generation. This is as safe as the JVM's {@link
     * java.util.UUID#randomUUID()} method, and includes 122 bits of random data. If you can test 1,000 tokens per
     * second (a token every millisecond), the expected time to generate a matching token is around 5e27 years.
     *
     * @return a UUID-shaped token.
     */
    public static SecretGenerator uuidGenerator() {
        return () -> UUID.randomUUID().toString();
    }

    /**
     * Generate a new token.
     *
     * @return the generated token.
     */
    public String generate();
}
