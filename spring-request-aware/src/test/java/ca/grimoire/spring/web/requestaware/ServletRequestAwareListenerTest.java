package ca.grimoire.spring.web.requestaware;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;

import org.junit.After;
import org.junit.Test;

public class ServletRequestAwareListenerTest {
	@After
	public void clearPreviousContext() {
		// Fucking globals.
		ServletRequestHolder.resetRequest();
	}

	@Test
	public void storesRequest() {
		ServletRequest request = mock(ServletRequest.class);
		ServletRequestEvent event = mock(ServletRequestEvent.class);
		when(event.getServletRequest()).thenReturn(request);

		ServletRequestAwareListener listener = new ServletRequestAwareListener();
		listener.requestInitialized(event);

		assertEquals(request, ServletRequestHolder.getRequest());
	}

	@Test
	public void resetsRequest() {
		ServletRequest request = mock(ServletRequest.class);
		ServletRequestEvent event = mock(ServletRequestEvent.class);
		when(event.getServletRequest()).thenReturn(request);

		ServletRequestAwareListener listener = new ServletRequestAwareListener();
		listener.requestInitialized(event);

		listener.requestDestroyed(event);
		try {
			ServletRequestHolder.getRequest();
			fail();
		} catch (IllegalStateException expected) {
			// Success!
		}
	}
}
