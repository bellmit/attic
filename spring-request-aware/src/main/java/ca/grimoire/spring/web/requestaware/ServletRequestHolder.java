package ca.grimoire.spring.web.requestaware;

import javax.servlet.ServletRequest;

import org.springframework.web.context.request.RequestContextHolder;

/**
 * A thread local holder for the current request, modelled after Spring's own
 * {@link RequestContextHolder}. This is primarily an implementation detail for
 * {@link ServletRequestAware}, and has the same caveats.
 * <p>
 * This class is not intended for direct use by any classes outside of this
 * package. Cheating and touching it directly will only hurt you.
 */
class ServletRequestHolder {
	// So here's the deal: the Spring guys feel you should get the request in
	// via a parameter and pass it around to where it needs to be. Normally, I
	// agree with them. However, I'm impementing some infrastructure to support
	// a multi-tenant app where the current request is necessary to determine
	// which tenant is in use, while trying to write the code "as if" it were a
	// single-tenant application.
	//
	// Maybe that's insane, but I think it's worth trying. Anyways, that's why
	// there are Stupid Global Threadlocals here. Code that depends on this
	// doesn't depend on the global-ness of it.

	private static final ThreadLocal<ServletRequest> currentRequest = new ThreadLocal<ServletRequest>();

	public static void setRequest(ServletRequest request) {
		if (request == null)
			throw new IllegalArgumentException("request");
		if (currentRequest.get() != null)
			throw new IllegalStateException(
					"A ServletRequest is already in progress.");
		currentRequest.set(request);
	}

	public static ServletRequest getRequest() {
		ServletRequest servletRequest = currentRequest.get();
		if (servletRequest != null)
			return servletRequest;
		throw new IllegalStateException("No ServletRequest in progress.");
	}

	public static void resetRequest() {
		currentRequest.set(null);
	}
}
