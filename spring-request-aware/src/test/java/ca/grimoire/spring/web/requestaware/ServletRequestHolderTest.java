package ca.grimoire.spring.web.requestaware;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletRequest;

import org.junit.After;
import org.junit.Test;

public class ServletRequestHolderTest {
	@After
	public void clearPreviousRequest() {
		// Fucking globals.
		ServletRequestHolder.resetRequest();
	}

	@Test
	public void holdsRequest() {
		ServletRequest request = mock(ServletRequest.class);

		ServletRequestHolder.setRequest(request);
		assertSame(request, ServletRequestHolder.getRequest());
	}

	@Test(expected = IllegalArgumentException.class)
	public void holdsNoNullRequest() {
		ServletRequestHolder.setRequest(null);
	}

	@Test
	public void holdsOneRequest() {
		ServletRequest request = mock(ServletRequest.class);
		ServletRequest request2 = mock(ServletRequest.class);

		ServletRequestHolder.setRequest(request);
		try {
			ServletRequestHolder.setRequest(request2);
			fail();
		} catch (IllegalStateException expected) {
			// Success! Almost.
			assertSame(request, ServletRequestHolder.getRequest());
		}
	}

	@Test
	public void resetClearsRequest() {
		ServletRequest request = mock(ServletRequest.class);

		ServletRequestHolder.setRequest(request);
		ServletRequestHolder.resetRequest();

		try {
			ServletRequestHolder.getRequest();
			fail();
		} catch (IllegalStateException expected) {
			// Success!
		}
	}

	@Test
	public void requestIsPerThread() throws Throwable {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			ServletRequest request = mock(ServletRequest.class);

			ServletRequestHolder.setRequest(request);

			Future<Void> task = executor.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					try {
						ServletRequestHolder.getRequest();
						fail();
					} catch (IllegalStateException expected) {
						// Success!
					}
					return null;
				}
			});
			try {
				task.get();
			} catch (ExecutionException expectedExecutionException) {
				try {
					throw expectedExecutionException.getCause();
				} catch (IllegalStateException expected) {
					// Success!
				}
			}
		} finally {
			executor.shutdownNow();
		}
	}
}
