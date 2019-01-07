package ca.grimoire.spring.web.requestaware;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * A web application context listener that drives the
 * {@link ServletRequestAwareProcessor} interface and associated machinery.
 * <p>
 * To deploy this listener, add the following to your <code>web.xml</code>:
 * 
 * <pre>
 * &lt;listener&gt;
 *   &lt;listener-class&gt;ca.grimoire.spring.web.requestaware.ServletRequestAwareListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </pre>
 * 
 * @see ServletRequestAwareProcessor
 */
public class ServletRequestAwareListener implements ServletRequestListener {

	/**
	 * Resets the current thread's in-flight {@link ServletRequest}.
	 * 
	 * @see javax.servlet.ServletRequestListener#requestDestroyed(javax.servlet.ServletRequestEvent)
	 */
	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		ServletRequestHolder.resetRequest();
	}

	/**
	 * Sets the current thread's in-flight {@link ServletRequest} to the request
	 * associated with this event, if no request is already in flight.
	 * 
	 * @see javax.servlet.ServletRequestListener#requestInitialized(javax.servlet.ServletRequestEvent)
	 */
	@Override
	public void requestInitialized(ServletRequestEvent event) {
		ServletRequestHolder.setRequest(event.getServletRequest());
	}

}
