package io.github.unacceptable.alias;

public final class Generators {

    private Generators() {
        // utility class
    }

    public static Generator<String> defaultStringGeneratorWrappers(final Generator<String> generator) {
        return new EmptyWrappingGenerator(new AbsentWrappingGenerator<>(new LiteralWrappingGenerator(generator)));
    }
}
