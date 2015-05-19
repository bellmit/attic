package com.loginbox.transactor.transactable;

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
