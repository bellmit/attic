package com.loginbox.transactor.transactable;

/**
 * A transactable action. An action has only side effects on the context: implementations should generally not use
 * out-of-band mechanisms (fields, globals, etc) to apply other side effects.
 *
 * @param <C>
 *         the context type.
 * @see com.loginbox.transactor.Transactor#execute(Action)
 */
@FunctionalInterface
public interface Action<C> {
    /**
     * Lifts a Runnable task into an action over some context. The context will be ignored by the resulting action. This
     * can be used to inject procedural steps such as logging into transactable sequences.
     *
     * @param task
     *         the task to lift to an Action.
     * @param <C>
     *         the context type of the resulting action.
     * @return an Action wrapping <var>task</var>.
     */
    public static <C> Action<C> lift(Runnable task) {
        return context -> task.run();
    }

    /**
     * Apply the action to a context.
     *
     * @param context
     *         the context to evaluate in.
     * @throws java.lang.Exception
     *         if the acton cannot be completed.
     */
    public void execute(C context) throws Exception;

    /**
     * Sequence this action before another action.
     *
     * @param next
     *         the action to evaluate next.
     * @return a new composite action that executes this action, and then executes the <var>next</var> action.
     */
    public default Action<C> andThen(Action<? super C> next) {
        return context -> {
            this.execute(context);
            next.execute(context);
        };
    }

    /**
     * Sequence this action before a query.
     *
     * @param query
     *         the query to evaluate after this action.
     * @param <R>
     *         the result type of the query.
     * @return a new composite query that executes this action, and then fetches <var>query</var> from the context.
     */
    public default <R> Query<C, R> before(Query<? super C, ? extends R> query) {
        return context -> {
            this.execute(context);
            R result = query.fetch(context);
            return result;
        };
    }
}
