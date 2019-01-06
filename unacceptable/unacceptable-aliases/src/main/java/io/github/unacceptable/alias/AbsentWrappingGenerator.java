package io.github.unacceptable.alias;

public class AbsentWrappingGenerator<T> implements Generator<T> {

    public static final String ABSENT = "ABSENT";

    private final Generator<T> delegate;

    public AbsentWrappingGenerator(final Generator<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T generate(final String alias) {
        if (ABSENT.equals(alias)) {
            return null;
        }
        return delegate.generate(alias);
    }
}
