package ca.grimoire.mainspring;

import java.awt.EventQueue;

/**
 * Bootstraps a Spring context XML file on the Swing EDT. The actual work of
 * bootstrapping the application is handled by {@link Bootstrap}; this class's
 * {@link #main(String[])} schedules the bootstrap onto the Swing EDT.
 * <p>
 * Like Bootstrap, this class can be specified as the Main-Class of a JAR file.
 * 
 * @see Bootstrap
 */
public class Swingstrap {
	/**
	 * Runs the bootstrap process on the Swing EDT. For details about
	 * command-line parameters, see {@link Bootstrap#main(String[])}.
	 * 
	 * @param args
	 *            the names of the contexts to load. If empty or
	 *            <code>null</code>, the default context will be loaded instead.
	 * @see Bootstrap#main(String[])
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Bootstrap.main(args);
			}
		});
	}
}
