package ca.grimoire.mainspring.daemon;

import org.apache.commons.daemon.DaemonContext;

/**
 * Marker interface for objects that require a {@link DaemonContext} to
 * function.
 */
public interface DaemonContextAware {

    /**
     * Provides a {@link DaemonContext} to the object.
     * 
     * @param daemonContext
     *            the daemon context in use.
     */
    public void setDaemonContext(DaemonContext daemonContext);

}
