package io.github.unacceptable.alias;

public class EmptyWrappingGenerator implements Generator<String> {

    public static final String ABSENT = "ABSENT";

    private final Generator<String> delegate;

    public EmptyWrappingGenerator(final Generator<String> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String generate(final String alias) {
        if (alias != null && alias.isEmpty()) {
            return "";
        }
        return delegate.generate(alias);
    }
}
