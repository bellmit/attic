package com.loginbox.transactor.transactable;

import java.util.function.Consumer;

/**
 * A transactable task that consumes a value in a context.
 *
 * @param <C>
 *         the context type.
 * @param <V>
 *         the value type consumed by the sink.
 * @see com.loginbox.transactor.Transactor#consume(Sink, Object)
 */
@FunctionalInterface
public interface Sink<C, V> {
    /**
     * Lifts a Consumer into a Sink over some context. The resulting sink will ignore its context, but can be composed
     * with other transactables.
     *
     * @param consumer
     *         the consumer to lift.
     * @param <C>
     *         the type of the resulting sink's context.
     * @param <V>
     *         the type of values to consume.
     * @return a Sink wrapping <var>consumer</var>.
     */
    public static <C, V> Sink<C, V> lift(Consumer<? super V> consumer) {
        return (context, value) -> consumer.accept(value);
    }

    /**
     * Consumes a value in the context.
     *
     * @param context
     *         the context to operate on.
     * @param value
     *         the value to consume.
     * @throws Exception
     *         if the sink cannot complete.
     */
    public void consume(C context, V value) throws Exception;
}
