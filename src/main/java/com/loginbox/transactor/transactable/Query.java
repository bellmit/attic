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
     * Fetches a value from a context.
     *
     * @param context
     *         the context.
     * @return the result of the query.
     * @throws java.lang.Exception
     *         if the query cannot be completed.
     */
    public R fetch(C context) throws Exception;


}
