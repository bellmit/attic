package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import com.loginbox.transactor.Transactables;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MergeTransposeTest {
    private final Transactables transactables = mock(Transactables.class);

    private final Context context = mock(Context.class);

    @SuppressWarnings("unchecked")
    private final Merge<Context, String, String, String> original = transactables::merge;
    private final Merge<Context, String, String, String> pivoted = original.transpose();

    @Test
    public void executesFunction() throws Exception {
        doReturn("result").when(transactables).merge(context, "left", "right");

        assertThat(pivoted.merge(context, "right", "left"), is("result"));

        verify(transactables).merge(context, "left", "right");
    }

    @Test
    public void propagatesFailures() throws Exception {
        RuntimeException taskFailed = new RuntimeException("merge failed");
        doThrow(taskFailed).when(transactables).merge(context, "left", "right");

        try {
            pivoted.merge(context, "right", "left");
            fail();
        } catch (RuntimeException caught) {
            assertThat(caught, is(taskFailed));
        }

        verify(transactables).merge(context, "left", "right");
    }
}