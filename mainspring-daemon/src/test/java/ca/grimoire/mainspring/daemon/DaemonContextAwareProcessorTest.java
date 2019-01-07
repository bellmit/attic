package ca.grimoire.mainspring.daemon;

import static org.junit.Assert.assertSame;

import org.apache.commons.daemon.DaemonContext;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class DaemonContextAwareProcessorTest {

    private final Mockery mockery = new JUnit4Mockery();

    private DaemonContext createFakeContext() {
        return mockery.mock(DaemonContext.class);
    }

    @Test
    public void nonDaemonContextAwareObject() {
        Object o = new Object();

        DaemonContextAwareProcessor processor = new DaemonContextAwareProcessor(
                createFakeContext());

        assertSame(o, processor.postProcessBeforeInitialization(o, "foo"));
        assertSame(o, processor.postProcessAfterInitialization(o, "foo"));
    }

    @Test
    public void injectsDaemonContext() {
        final DaemonContextAware bean = mockery.mock(DaemonContextAware.class);
        final DaemonContext fakeContext = createFakeContext();

        DaemonContextAwareProcessor processor = new DaemonContextAwareProcessor(
                fakeContext);

        mockery.checking(new Expectations() {
            {
                one(bean).setDaemonContext(fakeContext);
            }
        });

        assertSame(bean, processor.postProcessBeforeInitialization(bean, "foo"));
    }

    @Test
    public void doesNothingAfterInit() {
        final DaemonContextAware bean = mockery.mock(DaemonContextAware.class);
        final DaemonContext fakeContext = createFakeContext();

        DaemonContextAwareProcessor processor = new DaemonContextAwareProcessor(
                fakeContext);

        assertSame(bean, processor.postProcessAfterInitialization(bean, "foo"));
    }
}
