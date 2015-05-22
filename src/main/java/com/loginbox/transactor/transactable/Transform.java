package com.loginbox.transactor.transactable;

import java.util.function.Function;

/**
 * A transactable transform. Given a value of type <var>I</var> and a context of type <var>C</var>, a transform produces
 * a result of type <var>O</var>.
 *
 * @param <C>
 *         the context type; generally some variety of external resource, such as a database connection.
 * @param <I>
 *         the input type.
 * @param <O>
 *         the output type.
 * @see com.loginbox.transactor.Transactor#apply(Transform, Object)
 */
@FunctionalInterface
public interface Transform<C, I, O> {
    /**
     * Lifts a non-transactable function into a transform. The resulting transform ignores its context, but can be
     * composed with other transactables.
     *
     * @param function
     *         the function to lift.
     * @param <C>
     *         the context type for the resulting transform.
     * @param <I>
     *         the transform's input type.
     * @param <O>
     *         the transform's output type.
     * @return a transform wrapping <var>function</var>.
     */
    public static <C, I, O> Transform<C, I, O> lift(Function<? super I, ? extends O> function) {
        return (context, input) -> function.apply(input);
    }

    /**
     * Transform <var>value</var> in the context of <var>context</var>.
     *
     * @param context
     *         the external context of the transform.
     * @param value
     *         the value to transform.
     * @return the result of transforming this value.
     * @throws java.lang.Exception
     *         if the transform cannot be completed.
     */
    public O apply(C context, I value) throws Exception;

    /**
     * Sequence this transform before another action.
     *
     * @param next
     *         the action to evaluate next.
     * @return a new composite transform that applies this transform to obtain the result, and then executes the
     * <var>next</var> action before returning it.
     */
    public default Transform<C, I, O> andThen(Action<? super C> next) {
        return (context, value) -> {
            O result = this.apply(context, value);
            next.execute(context);
            return result;
        };
    }

    /**
     * Appends a transform to this transform. The resulting transform is effectively the composition of this and
     * <var>next</var>, applied to the same context.
     *
     * @param next
     *         the transform to append to this transform.
     * @param <P>
     *         the result type of <var>next</var>, and the resulting composite transform.
     * @return a composite transform that obtains an interim result from this, and a final result by transforming the
     * interim result through <var>next</var>.
     */
    public default <P> Transform<C, I, P> transformedBy(Transform<? super C, ? super O, ? extends P> next) {
        return (context, input) -> {
            O interim = this.apply(context, input);
            P result = next.apply(context, interim);
            return result;
        };
    }

    /**
     * Consumes the result of this transform using a sink. The resulting sink first transforms its input through this
     * transform, then passes the result to <var>sink</var>.
     *
     * @param sink
     *         the sink to consume the result of this transform.
     * @return a composite sink that transforms its input using this, then consumes it with <var>sink</var>.
     */
    public default Sink<C, I> consumedBy(Sink<? super C, ? super O> sink) {
        return (context, input) -> {
            O interim = this.apply(context, input);
            sink.consume(context, interim);
        };
    }

    /**
     * Transform the left argument to a merge through this transform.
     *
     * @param merge
     *         the merge to compose with.
     * @param <MN>
     *         the type of the merge's right argument.
     * @param <MO>
     *         the merge's result type.
     * @return a new merge, whose left argument will be modified by this transform.
     */
    public default <MN, MO> Merge<C, I, MN, MO> intoLeft(Merge<? super C, ? super O, ? super MN, ? extends MO> merge) {
        return (context, left, right) -> {
            O interim = this.apply(context, left);
            MO result = merge.merge(context, interim, right);
            return result;
        };
    }

    /**
     * Transform the right argument to a merge through this transform.
     *
     * @param merge
     *         the merge to compose with.
     * @param <MM>
     *         the type of the merge's left argument.
     * @param <MO>
     *         the merge's result type.
     * @return a new merge, whose right argument will be modified by this transform.
     */
    public default <MM, MO> Merge<C, MM, I, MO> intoRight(Merge<? super C, ? super MM, ? super O, ? extends MO> merge) {
        return (context, left, right) -> {
            O interim = this.apply(context, right);
            MO result = merge.merge(context, left, interim);
            return result;
        };
    }
}
