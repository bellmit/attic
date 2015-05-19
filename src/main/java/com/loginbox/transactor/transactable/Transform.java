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
}
