package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ActionLiftTest {
    private final Context context = mock(Context.class);

    private final Runnable task = mock(Runnable.class);
    private final Action<Context> lifted = Action.lift(task);

    @Test
    public void executesTask() throws Exception {
        lifted.execute(context);

        verify(task).run();
    }

    @Test
    public void propagatesFailures() throws Exception {
        RuntimeException taskFailed = new RuntimeException("task failed");
        doThrow(taskFailed).when(task).run();

        try {
            lifted.execute(context);
            fail();
        } catch (RuntimeException caught) {
            assertThat(caught, is(taskFailed));
        }

        verify(task).run();
    }
}