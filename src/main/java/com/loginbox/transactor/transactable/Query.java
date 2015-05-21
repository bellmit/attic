package com.loginbox.transactor.transactable;

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
