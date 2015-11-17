package io.github.unacceptable.alias;

public class LiteralWrappingGenerator implements Generator<String> {

    private final Generator<String> delegate;

    public LiteralWrappingGenerator(final Generator<String> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String generate(final String alias) {
        if (alias != null && alias.startsWith("<") && alias.endsWith(">")) {
            return alias.substring(1, alias.length() - 1);
        }
        return delegate.generate(alias);
    }
}
