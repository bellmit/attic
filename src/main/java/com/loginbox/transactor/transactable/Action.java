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
     * Apply the action to a context.
     *
     * @param context
     *         the context to evaluate in.
     * @throws java.lang.Exception
     *         if the acton cannot be completed.
     */
    public void execute(C context) throws Exception;
}
