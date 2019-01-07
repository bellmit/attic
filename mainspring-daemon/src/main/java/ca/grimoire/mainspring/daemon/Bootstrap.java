package ca.grimoire.mainspring.daemon;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Bootstraps a Spring context XML file. The context configuration can either be
 * loaded from a classpath resource using a
 * {@link ClassPathXmlApplicationContext}.
 * <p>
 * To do something useful with this class, create a context containing at least
 * one non-lazy <code>singleton</code>-scoped bean definition with an
 * <code>init-method</code> specified.
 */
public class Bootstrap implements Daemon {
    /** The name of the Spring context XML file loaded by default. */
    public static final String DEFAULT_CONTEXT = "META-INF/mainspring.xml";
    private DaemonContext daemonContext;

    /**
     * Destroys the daemon. The current implementation does nothing.
     */
    public void destroy() {

    }

    /**
     * Prepares the daemon. The current implementation does nothing.
     */
    public void init(DaemonContext context) {
        this.daemonContext = context;
    }

    /**
     * Runs the bootstrap process. The bootstrap configuration is loaded from
     * <code>{@value #DEFAULT_CONTEXT}</code> as a
     * {@link ClassPathXmlApplicationContext resource} and started.
     */
    public void start() throws Exception {
        if (context != null)
            throw new IllegalStateException("already running");
        context = new ClassPathXmlApplicationContext();
        context.setConfigLocation(DEFAULT_CONTEXT);
        context.afterPropertiesSet();
    }

    /**
     * Shuts down the daemon. The Spring context created in {@link #start()} is
     * shut down cleanly.
     */
    public void stop() {
        if (context != null)
            context.close();
        context = null;
    }

    private AbstractXmlApplicationContext context = null;
}