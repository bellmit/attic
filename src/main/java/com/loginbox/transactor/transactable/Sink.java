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

    /**
     * Pivots this sink's context and argument. Given a sink with context C and argument V, returns a new sink with
     * context V and argument C.
     *
     * @return the pivoted version of this sink.
     */
    public default Sink<V, C> pivot() {
        return (context, value) -> consume(value, context);
    }

    /**
     * Sequence this sink before another action.
     *
     * @param next
     *         the action to evaluate next.
     * @return a new composite sink that executes this sink, and then executes the <var>next</var> action.
     */
    public default Sink<C, V> andThen(Action<? super C> next) {
        return (context, value) -> {
            this.consume(context, value);
            next.execute(context);
        };
    }

    /**
     * Sequence this sink before a query. The result is, in a perverse way, a transform.
     *
     * @param query
     *         the query to evaluate after this sink.
     * @param <R>
     *         the result type of the query, and the resulting transform.
     * @return a new composite transform that executes this sink to consume the input value, and then fetches the
     * resulting value using <var>query</var>.
     */
    public default <R> Transform<C, V, R> before(Query<? super C, ? extends R> query) {
        return (context, value) -> {
            this.consume(context, value);
            R result = query.fetch(context);
            return result;
        };
    }
}
