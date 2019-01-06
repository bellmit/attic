package io.github.unacceptable.alias;

@FunctionalInterface
public interface Generator<T> {
    T generate(String alias);
}
