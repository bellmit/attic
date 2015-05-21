package com.loginbox.transactor.transactable;

import java.util.function.BiFunction;

/**
 * A transactable transform. Given two values of type <var>M</var> and <var>N</var>, and a context of type <var>C</var>,
 * a merge produces a single result of type <var>O</var>.
 *
 * @param <C>
 *         the context type; generally some variety of external resource, such as a database connection.
 * @param <M>
 *         the first input type.
 * @param <N>
 *         the second input type.
 * @param <O>
 *         the output type.
 * @see com.loginbox.transactor.Transactor#combine(Merge, Object, Object)
 */
@FunctionalInterface
public interface Merge<C, M, N, O> {
    /**
     * Lifts a non-transactable binary function into a merge. The resulting merge ignores its context, but can be
     * composed with other transactables.
     *
     * @param function
     *         the function to lift.
     * @param <C>
     *         the context type for the resulting merge.
     * @param <M>
     *         the first input type.
     * @param <N>
     *         the second input type.
     * @param <O>
     *         the merge's output type.
     * @return a merge wrapping <var>function</var>.
     */
    public static <C, M, N, O> Merge<C, M, N, O> lift(BiFunction<? super M, ? super N, ? extends O> function) {
        return (context, left, right) -> function.apply(left, right);
    }

    /**
     * Merge <var>left</var> and <var>right</var> in the context of <var>context</var>.
     *
     * @param context
     *         the external context of the merge.
     * @param left
     *         the first of the values to merge.
     * @param right
     *         the second of the values to merge.
     * @return the result of merging these values.
     * @throws java.lang.Exception
     *         if the merge cannot be completed.
     */
    public O merge(C context, M left, N right) throws Exception;
}
