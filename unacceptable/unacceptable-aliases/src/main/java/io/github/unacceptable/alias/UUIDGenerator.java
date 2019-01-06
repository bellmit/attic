package io.github.unacceptable.alias;

import java.util.UUID;

public class UUIDGenerator implements Generator<UUID> {
    private static final UUIDGenerator GENERATOR = new UUIDGenerator();
    private final Generator<UUID> generator;

    /**
     * @param alias
     *         an alias for the generated value. The alias is unused in this generator, but can be used by an {@link
     *         AliasStore} to associate the UUID with a name.
     * @return a randomly-generated UUID.
     */
    public static UUID defaultGenerate(String alias) {
        return GENERATOR.generate(alias);
    }

    public UUIDGenerator() {
        this.generator = new AbsentWrappingGenerator<>(this::generateUUID);
    }

    /**
     * @param alias
     *         an alias for the generated value. The alias is unused in this generator, but can be used by an {@link
     *         AliasStore} to associate the UUID with a name.
     * @return a randomly-generated UUID.
     */
    @Override
    public UUID generate(final String alias) {
        return generator.generate(alias);
    }

    /**
     * @param alias
     *         an alias for the generated value. The alias is unused in this generator, but can be used by an {@link
     *         AliasStore} to associate the UUID with a name.
     * @return a randomly-generated UUID.
     */
    public UUID generateUUID(String alias) {
        return UUID.randomUUID();
    }
}
