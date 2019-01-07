package ca.grimoire.mainloop;

import java.util.concurrent.RejectedExecutionException;

/**
 * Allows mainloops to emit notifications when they terminate.
 */
public interface TerminationListener {

    /**
     * Called when an {@link ExecutorMainloop} terminates naturally.
     * 
     * @param reason
     *            the exception that caused the mainloop to terminate. May be
     *            <code>null</code>.
     */
    public void mainloopExited(RejectedExecutionException reason);
}
