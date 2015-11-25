package io.github.unacceptable.alias;

import java.util.UUID;

public class UUIDGenerator implements Generator<UUID> {
    private static final UUIDGenerator GENERATOR = new UUIDGenerator();
    private final Generator<UUID> generator;

    /**
     * Generate a random UUID.
     */
    public static UUID defaultGenerate(String alias) {
        return GENERATOR.generate(alias);
    }

    public UUIDGenerator() {
        this.generator = new AbsentWrappingGenerator<>(this::generateUUID);
    }

    @Override
    public UUID generate(final String alias) {
        return generator.generate(alias);
    }

    /**
     * Generate a random UUID.
     */
    public UUID generateUUID(String alias) {
        return UUID.randomUUID();
    }
}
