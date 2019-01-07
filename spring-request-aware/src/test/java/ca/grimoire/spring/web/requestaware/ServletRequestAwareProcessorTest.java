package ca.grimoire.spring.web.requestaware;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.servlet.ServletRequest;

import org.junit.After;
import org.junit.Test;

public class ServletRequestAwareProcessorTest {
	@After
	public void clearPreviousContext() {
		// Fucking globals.
		ServletRequestHolder.resetRequest();
	}

	@Test
	public void injectsRequest() {
		ServletRequestAware bean = mock(ServletRequestAware.class);
		ServletRequest request = mock(ServletRequest.class);

		ServletRequestHolder.setRequest(request);

		ServletRequestAwareProcessor processor = new ServletRequestAwareProcessor();
		processor.postProcessBeforeInitialization(bean, "name");

		verify(bean).setServletRequest(request);
	}
}
