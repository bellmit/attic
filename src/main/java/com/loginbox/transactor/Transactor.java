package com.loginbox.transactor;

import com.loginbox.transactor.transactable.Action;
import com.loginbox.transactor.transactable.Merge;
import com.loginbox.transactor.transactable.Query;
import com.loginbox.transactor.transactable.Sink;
import com.loginbox.transactor.transactable.Transform;

import java.util.concurrent.Callable;

/**
 * Evaluates transactables in a context.
 * <p>
 * Subclasses must provide three methods to manage the context itself. In return, this class will automatically open the
 * context, apply transactables, clean up the context, and return any results. This pattern is appropriate for things
 * like database transactions.
 *
 * @param <C>
 *         the context type.
 */
public abstract class Transactor<C extends AutoCloseable> {
    /**
     * Opens a new context at the start of an execution.
     *
     * @return a context object.
     * @throws java.lang.Exception
     *         if a context cannot be created. This will be propagated out of the associated transacted method as-is.
     */
    protected abstract C createContext() throws Exception;

    /**
     * Invoked at the end of an execution to clean up the context if and only if the execution raises an exception. The
     * default implementation is to do nothing when a transaction fails. Most applications will want to override this to
     * perform cleanup actions, such as rolling back transactions.
     *
     * @param context
     *         the context for the execution. This will be the context previously obtained from {@link
     *         #createContext()}.
     * @throws java.lang.Exception
     *         if a context cannot be aborted. This will be propagated out of the associated transacted method as-is.
     *         Transactor itself makes no assumptions about whether the transaction is valid, but will still attempt to
     *         {@link java.lang.AutoCloseable#close() close it}. This exception will be {@link Throwable#getSuppressed()
     *         suppressed} by the exception that caused the operation to abort in the first place.
     */
    protected void abort(C context) throws Exception {
        /* by default, do nothing */
    }

    /**
     * Invoked at the end of an execution to clean up the context if and only if the execution completes normally. The
     * default implementation is to do nothing when a transaction fails. Most applications will want to override this to
     * perform post-success actions, such as committing transactions.
     *
     * @param context
     *         the context for the execution. This will be the context previously obtained from {@link
     *         #createContext()}.
     * @throws java.lang.Exception
     *         if a context cannot be finished successfully. This will be propagated out of the associated transacted
     *         method as-is. Transactor itself makes no assumptions about whether the transaction is valid, but will
     *         still attempt to {@link java.lang.AutoCloseable#close() close it}.
     */
    protected void finish(C context) throws Exception {
        /* by default, do nothing */
    }

    /**
     * Runs an {@link com.loginbox.transactor.transactable.Action} in a new context.
     *
     * @param action
     *         the action to evaluate.
     * @throws Exception
     *         if the action fails, or if the transacted context fails.
     */
    public void execute(Action<? super C> action) throws Exception {
        try (C context = createContext()) {
            executeInContext(context, () -> {
                action.execute(context);
                return null;
            });
        }
    }

    /**
     * Runs a {@link com.loginbox.transactor.transactable.Query} in a new context, then returns the result of the
     * query.
     *
     * @param query
     *         the query to evaluate.
     * @param <R>
     *         the result type.
     * @return the query result.
     * @throws Exception
     *         if closing the context fails, or if the query fails.
     */
    public <R> R fetch(Query<? super C, ? extends R> query) throws Exception {
        try (C context = createContext()) {
            return executeInContext(context, () -> query.fetch(context));
        }
    }

    /**
     * Runs a {@link com.loginbox.transactor.transactable.Transform} in a new context given an initial value, then
     * returns the resulting value.
     *
     * @param transform
     *         the transform to evaluate.
     * @param value
     *         the value to pass through the transform.
     * @param <I>
     *         the type of the value to transform.
     * @param <O>
     *         the type of the result of the transform.
     * @return the result of the transform.
     * @throws Exception
     *         if closing the context fails, or if the transform fails.
     */
    public <I, O> O apply(Transform<? super C, ? super I, ? extends O> transform, I value) throws Exception {
        try (C context = createContext()) {
            return executeInContext(context, () -> transform.apply(context, value));
        }
    }

    /**
     * Runs a {@link com.loginbox.transactor.transactable.Sink} in a new context, consuming a value.
     *
     * @param sink
     *         the sink to evaluate.
     * @param value
     *         the value to consume.
     * @param <V>
     *         the type of the value to consume.
     * @throws Exception
     *         if closing the context fails, or if the sink fails.
     */
    public <V> void consume(Sink<? super C, ? super V> sink, V value) throws Exception {
        try (C context = createContext()) {
            executeInContext(context, () -> {
                sink.consume(context, value);
                return null;
            });
        }
    }

    /**
     * Runs a {@link com.loginbox.transactor.transactable.Merge} in a new context given an initial pair of values, then
     * returns the resulting value.
     *
     * @param merge
     *         the merge to evaluate.
     * @param left
     *         the left value to pass through the merge.
     * @param right
     *         the right value to pass through the merge.
     * @param <M>
     *         the type of the left value of the merge.
     * @param <N>
     *         the type of the right value of the merge.
     * @param <O>
     *         the type of the result of the merge.
     * @return the result of the merge.
     * @throws Exception
     *         if closing the context fails, or if the merge fails.
     */
    public <M, N, O> O combine(Merge<? super C, ? super M, ? super N, ? extends O> merge, M left, N right) throws Exception {
        try (C context = createContext()) {
            return executeInContext(context, () -> merge.merge(context, left, right));
        }
    }

    private <R> R executeInContext(C context, Callable<R> task) throws Exception {
        R result = executeOrAbort(context, task);
        finish(context);
        return result;
    }

    private <R> R executeOrAbort(C context, Callable<R> task) throws Exception {
        try {
            return task.call();
        } catch (Exception taskFailed) {
            abortWithExistingFailure(context, taskFailed);
            throw taskFailed;
        }
    }

    private void abortWithExistingFailure(C context, Exception taskFailed) throws Exception {
        try {
            abort(context);
        } catch (Exception abortFailed) {
            taskFailed.addSuppressed(abortFailed);
        }
    }
}
