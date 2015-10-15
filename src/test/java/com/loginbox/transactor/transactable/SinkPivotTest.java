package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import com.loginbox.transactor.Transactables;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SinkPivotTest {
    private final Transactables transactables = mock(Transactables.class);

    private final Context context = mock(Context.class);

    @SuppressWarnings("unchecked")
    private final Sink<Context, String> original = transactables::sink;
    private final Sink<String, Context> pivoted = original.pivot();

    @Test
    public void executesFunction() throws Exception {
        pivoted.consume("value", context);

        verify(transactables).sink(context, "value");
    }

    @Test
    public void propagatesFailures() throws Exception {
        RuntimeException taskFailed = new RuntimeException("sink failed");
        doThrow(taskFailed).when(transactables).sink(context, "value");

        try {
            pivoted.consume("value", context);
            fail();
        } catch (RuntimeException caught) {
            assertThat(caught, is(taskFailed));
        }

        verify(transactables).sink(context, "value");
    }
}