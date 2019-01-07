package ca.grimoire.mainloop;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class ExecutorMainloopTest {

    @Test
    public void cleanShutdown() {
        final Runnable fakeTask = mockery.mock(Runnable.class);
        final Executor fakeExecutor = mockery.mock(Executor.class);

        final ExecutorMainloop mainloop = new ExecutorMainloop(fakeTask,
                fakeExecutor);

        final TerminationListener terminationListener = mockery
                .mock(TerminationListener.class);

        mainloop.addTerminationListener(terminationListener);

        mockery.checking(new Expectations() {
            {
                Sequence runSequence = mockery
                        .sequence("One iteration sequence");

                one(fakeTask).run();
                inSequence(runSequence);

                RejectedExecutionException terminationException = new RejectedExecutionException();
                one(fakeExecutor).execute(mainloop);
                will(throwException(terminationException));
                inSequence(runSequence);

                one(terminationListener).mainloopExited(terminationException);
                inSequence(runSequence);
            }
        });

        mainloop.run();
    }

    @Test
    public void kickoffSubmits() {
        final Runnable fakeTask = mockery.mock(Runnable.class);
        final Executor fakeExecutor = mockery.mock(Executor.class);

        final ExecutorMainloop mainloop = new ExecutorMainloop(fakeTask,
                fakeExecutor);

        mockery.checking(new Expectations() {
            {
                one(fakeExecutor).execute(mainloop);
            }
        });

        mainloop.start();
    }

    @Test
    public void runsAndResubmits() {
        final Runnable fakeTask = mockery.mock(Runnable.class);
        final Executor fakeExecutor = mockery.mock(Executor.class);

        final ExecutorMainloop mainloop = new ExecutorMainloop(fakeTask,
                fakeExecutor);

        mockery.checking(new Expectations() {
            {
                Sequence runSequence = mockery
                        .sequence("One iteration sequence");

                one(fakeTask).run();
                inSequence(runSequence);

                one(fakeExecutor).execute(mainloop);
                inSequence(runSequence);
            }
        });

        mainloop.run();
    }

    private final Mockery mockery = new JUnit4Mockery();
}
