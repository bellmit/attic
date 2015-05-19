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
}
