package ca.grimoire.mainspring;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Bootstraps a Spring context XML file. The context configuration can either be
 * loaded from a classpath resource using a
 * {@link ClassPathXmlApplicationContext}, which happens if no command-line
 * arguments are provided, or using a {@link FileSystemXmlApplicationContext},
 * which happens if any command-line arguments are provided. The context is
 * created, its shutdown hook is registered, and then the thread that loaded the
 * context terminates.
 * <p>
 * To do something useful with this class, create a context containing at least
 * one non-lazy <code>singleton</code>-scoped bean definition with an
 * <code>init-method</code> specified.
 * <p>
 * This class can be specified as the <code>Main-Class</code> of a JAR file.
 * This allows for JARs containing a <code>{@value #DEFAULT_CONTEXT}</code>
 * resource to be relatively self-contained; the JAR containing this class must
 * be mentioned in the <code>Class-Path</code>, either directly or indirectly,
 * along with the JARs for any Spring components used in the bootstrap context.
 * <p>
 * <strong>Do not use this class directly if your Spring application context
 * contains Swing components.</strong> The bootstrap process runs on the thread
 * that calls {@link #main(String[])}. See {@link Swingstrap} for a Swing-safe
 * way to launch Swing contexts.
 */
public class Bootstrap {
	/** The name of the Spring context XML file loaded by default. */
	public static final String DEFAULT_CONTEXT = "META-INF/mainspring.xml";

	/**
	 * Runs the bootstrap process. If no arguments are provided, the bootstrap
	 * configuration is loaded from <code>{@value #DEFAULT_CONTEXT}</code> as a
	 * {@link ClassPathXmlApplicationContext resource}. All arguments are
	 * treated as alternate context configuration
	 * {@link FileSystemXmlApplicationContext file}s and are loaded
	 * <em>instead of</em> the default context.
	 * 
	 * @param args
	 *            the names of the contexts to load. If empty or
	 *            <code>null</code>, the default context will be loaded instead.
	 */
	public static void main(String[] args) {
		AbstractXmlApplicationContext context = createContext(args);
		context.registerShutdownHook();
		context.afterPropertiesSet();
	}

	private static AbstractXmlApplicationContext createContext(String[] args) {
		if (args != null && args.length > 0)
			return createArgumentContext(args);
		return createDefaultContext();
	}

	private static AbstractXmlApplicationContext createArgumentContext(
			String[] args) {
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext();
		context.setConfigLocations(args);
		return context;
	}

	private static AbstractXmlApplicationContext createDefaultContext() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		context.setConfigLocation(DEFAULT_CONTEXT);
		return context;
	}
}
