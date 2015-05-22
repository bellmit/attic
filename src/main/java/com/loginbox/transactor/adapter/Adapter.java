package com.loginbox.transactor.adapter;

import com.loginbox.transactor.transactable.Action;
import com.loginbox.transactor.transactable.Merge;
import com.loginbox.transactor.transactable.Query;
import com.loginbox.transactor.transactable.Sink;
import com.loginbox.transactor.transactable.Transform;

/**
 * Converts contexts for transactables.
 *
 * @param <O>
 *         the outer (source) context type.
 * @param <I>
 *         the inner (result) context type.
 */
@FunctionalInterface
public interface Adapter<O, I> {
    /**
     * Converts an outer context into an inner context.
     *
     * @param outer
     *         the outer context.
     * @return an inner context.
     */
    public I convert(O outer);

    /**
     * Wraps an action, changing its context type. If the action's context type is <code>I</code>, the result's context
     * type will be <code>O</code>.
     *
     * @param action
     *         the action over <var>I</var>
     * @return an action over <var>O</var>
     */
    public default Action<O> around(Action<? super I> action) {
        return outer -> {
            I inner = convert(outer);
            action.execute(inner);
        };
    }

    /**
     * Wraps a sink, changing its context type. If the sink's context type is <code>I</code>, the result's context type
     * will be <code>O</code>.
     *
     * @param sink
     *         the sink over <var>I</var>
     * @param <V>
     *         the type of value consumed by the sink.
     * @return a sink over <var>O</var>
     */
    public default <V> Sink<O, V> around(Sink<? super I, ? super V> sink) {
        return (outer, value) -> {
            I inner = convert(outer);
            sink.consume(inner, value);
        };
    }

    /**
     * Wraps a query, changing its context type. If the query's context type is <code>I</code>, the result's context
     * type will be <code>O</code>.
     *
     * @param query
     *         a query over <var>I</var>
     * @param <R>
     *         the result type of the query.
     * @return a query over <var>O</var>
     */
    public default <R> Query<O, R> around(Query<? super I, R> query) {
        return outer -> {
            I inner = convert(outer);
            R result = query.fetch(inner);
            return result;
        };
    }

    /**
     * Wraps a transform, changing its context type. If the transform's context type is <code>I</code>, the result's
     * context type will be <code>O</code>.
     *
     * @param transform
     *         a transform over <var>I</var>
     * @param <A>
     *         the input type of the transform.
     * @param <B>
     *         the result type of the transform.
     * @return a transform over <var>O</var>
     */
    public default <A, B> Transform<O, A, B> around(Transform<? super I, A, B> transform) {
        return (outer, value) -> {
            I inner = convert(outer);
            B result = transform.apply(inner, value);
            return result;
        };
    }

    /**
     * Wraps a merge, changing its context type. If the merge's context type is <code>I</code>, the result's context
     * type will be <code>O</code>.
     *
     * @param merge
     *         a merge over <var>I</var>
     * @param <A>
     *         the left input type of the merge.
     * @param <B>
     *         the right input type of the merge.
     * @param <C>
     *         the output type of the merge.
     * @return a merge over <var>O</var>
     */
    public default <A, B, C> Merge<O, A, B, C> around(Merge<? super I, A, B, C> merge) {
        return (outer, left, right) -> {
            I inner = convert(outer);
            C result = merge.merge(inner, left, right);
            return result;
        };
    }
}
