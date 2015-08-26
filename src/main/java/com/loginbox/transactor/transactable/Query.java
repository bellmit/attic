package com.loginbox.transactor.transactable;

import java.util.function.Supplier;

/**
 * A transactable that obtains a value from a context.
 *
 * @param <C>
 *         the context type.
 * @param <R>
 *         the result type for the query.
 * @see com.loginbox.transactor.Transactor#fetch(Query)
 */
@FunctionalInterface
public interface Query<C, R> {
    /**
     * Lifts a Supplier into a Query over some context. The resulting query will ignore the context, but can be composed
     * with other transactables.
     *
     * @param supplier
     *         the supplier to lift.
     * @param <C>
     *         the context type of the resulting Query.
     * @param <R>
     *         the result type of the resulting query.
     * @return a Query wrapping <var>supplier</var>.
     */
    public static <C, R> Query<C, R> lift(Supplier<? extends R> supplier) {
        return context -> supplier.get();
    }

    /**
     * Creates a Query that returns a constant. This can be composed with other transactables, such as {@link Merge}s,
     * to inject external values into a complex transactor at the appropriate point.
     *
     * @param result
     *         the constant value to return.
     * @param <C>
     *         the context type of the resulting Query.
     * @param <R>
     *         the result type of the resulting Query.
     * @return a query which returns exactly <var>result</var> regardless of context.
     */
    public static <C, R> Query<C, R> constant(R result) {
        return context -> result;
    }

    /**
     * Fetches a value from a context.
     *
     * @param context
     *         the context.
     * @return the result of the query.
     * @throws java.lang.Exception
     *         if the query cannot be completed.
     */
    public R fetch(C context) throws Exception;

    /**
     * Sequence this query before an action.
     *
     * @param next
     *         the action to evaluate next.
     * @return a new composite query that fetches this query to obtain the result, and then executes the <var>next</var>
     * action before returning it.
     */
    public default Query<C, R> andThen(Action<? super C> next) {
        return context -> {
            R result = this.fetch(context);
            next.execute(context);
            return result;
        };
    }

    /**
     * Appends a transform to this query. The resulting query is effectively the composition of this and
     * <var>next</var>, applied to the same context.
     *
     * @param next
     *         the transform to append to this query.
     * @param <S>
     *         the result type of <var>next</var>, and the resulting composite query.
     * @return a composite query that obtains an interim result from this, and a final result by transforming the
     * interim result through <var>next</var>.
     */
    public default <S> Query<C, S> transformedBy(Transform<? super C, ? super R, ? extends S> next) {
        return context -> {
            R interim = this.fetch(context);
            S result = next.apply(context, interim);
            return result;
        };
    }

    /**
     * Consumes the result of this query using a sink. The resulting sink first obtains a value through this query, then
     * passes the result to <var>sink</var>.
     *
     * @param sink
     *         the sink to consume the result of this transform.
     * @return a composite action that obtains a value using this, then consumes it with <var>sink</var>.
     */
    public default Action<C> consumedBy(Sink<? super C, ? super R> sink) {
        return context -> {
            R interim = this.fetch(context);
            sink.consume(context, interim);
        };
    }

    /**
     * Combines this query with a merge, using the query result as the left argument to the merge. The result is a
     * transform whose <var>value</var> will be the right argument to the merge.
     *
     * @param merge
     *         the merge to combine this query with.
     * @param <N>
     *         the type of the merge's right argument.
     * @param <O>
     *         the merge's result type.
     * @return the resulting transform.
     */
    public default <N, O> Transform<C, N, O> intoLeft(Merge<C, ? super R, ? super N, ? extends O> merge) {
        return (context, value) -> {
            R intermediateResult = this.fetch(context);
            O result = merge.merge(context, intermediateResult, value);
            return result;
        };
    }

    /**
     * Combines this query with a merge, using the query result as the right argument to the merge. The result is a
     * transform whose <var>value</var> will be the left argument to the merge.
     *
     * @param merge
     *         the merge to combine this query with.
     * @param <M>
     *         the type of the merge's second argument.
     * @param <O>
     *         the merge's result type.
     * @return the resulting transform.
     */
    public default <M, O> Transform<C, M, O> intoRight(Merge<C, ? super M, ? super R, ? extends O> merge) {
        return (context, value) -> {
            R intermediateResult = this.fetch(context);
            O result = merge.merge(context, value, intermediateResult);
            return result;
        };
    }
}
