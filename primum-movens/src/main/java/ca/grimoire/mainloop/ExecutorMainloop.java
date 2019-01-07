package ca.grimoire.mainloop;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import ca.grimoire.events.Dispatcher;
import ca.grimoire.events.Firer;

/**
 * Schedules a runnable indefinitely. Every time the runnable completes, it's
 * resubmitted to the executor. Ensures that the runnable is only ever in
 * progress once (per call to {@link #start()}) but not that it's always run on
 * the same thread.
 * <p>
 * Once initiated, this mainloop type can only be stopped by shutting down the
 * associated executor.
 */
public class ExecutorMainloop implements Runnable {

    /**
     * Prepares to run a task on a given executor.
     * 
     * @param task
     *            the task to schedule.
     * @param executor
     *            the executor to run against.
     */
    public ExecutorMainloop(Runnable task, Executor executor) {
        this.task = task;
        this.executor = executor;
    }
    /**
     * Registers a listener to be notified when the main loop exits. The
     * listener will be invoked if the main loop terminates after being rejected
     * by the executor.
     * 
     * @param terminationListener
     *            the listener to notify.
     */
    public void addTerminationListener(TerminationListener terminationListener) {
        terminationDispatcher.addListener(terminationListener);
    }
    /**
     * Unregisters a listener added by
     * {@link #addTerminationListener(TerminationListener)}.
     * 
     * @param terminationListener
     *            the listener to deregister.
     */
    public void removeTerminationListener(
            TerminationListener terminationListener) {
        terminationDispatcher.removeListener(terminationListener);
    }

    /**
     * Runs the task attached to the mainloop object, then resubmits it to the
     * associated executor. This is normally called by the executor that was
     * passed to {@link #ExecutorMainloop(Runnable, Executor)}, and should not
     * normally be called directly; however, this can be used as an alternative
     * to {@link #start()} where the caller wants to run the associated task on
     * the current thread (once) before passing control off to the executor.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        task.run();
        resubmit();
    }

    /**
     * Kicks off the main loop by submitting it to the executor passed to
     * {@link #ExecutorMainloop(Runnable, Executor)}.
     */
    public void start() {
        resubmit();
    }

    private void resubmit() {
        try {
            executor.execute(this);
        } catch (final RejectedExecutionException ree) {
            terminationDispatcher.fire(new Firer<TerminationListener>() {
                public void fire(TerminationListener listener) {
                    listener.mainloopExited(ree);
                }
            });
        }
    }

    private final Executor executor;
    private final Runnable task;
    private final Dispatcher<TerminationListener> terminationDispatcher = new Dispatcher<TerminationListener>();

}
