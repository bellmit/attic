package ca.grimoire.spring.web.requestaware;

import javax.servlet.ServletRequest;

import org.springframework.web.context.ServletContextAware;

/**
 * An injection target interface similar to Spring's own
 * {@link ServletContextAware} which allows beans to indicate a dependency on a
 * servlet request. Generally, this should only be used for infrastructure that
 * depends on per-request state; application logic should pass parameters using
 * normal method calls.
 * <p>
 * Bean definitions for objects implementing this method should be in request
 * scope in your Spring configuration. Other scopes are possible, but difficult
 * to maintain. See truth #3.
 */
public interface ServletRequestAware {
	/**
	 * Provides the current {@link ServletRequest}.
	 * 
	 * @param servletRequest
	 *            the currently-executing {@link ServletRequest}.
	 */
	public void setServletRequest(ServletRequest servletRequest);
}
