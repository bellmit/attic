package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import org.junit.Test;

import java.util.function.BiFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MergeLiftTest {
    private final Context context = mock(Context.class);

    @SuppressWarnings("unchecked")
    private final BiFunction<String, String, String> function = mock(BiFunction.class);
    private final Merge<Context, String, String, String> lifted = Merge.lift(function);

    @Test
    public void executesFunction() throws Exception {
        doReturn("result").when(function).apply("left", "right");

        assertThat(lifted.merge(context, "left", "right"), is("result"));

        verify(function).apply("left", "right");
    }

    @Test
    public void propagatesFailures() throws Exception {
        RuntimeException taskFailed = new RuntimeException("function failed");
        doThrow(taskFailed).when(function).apply("left", "right");

        try {
            lifted.merge(context, "left", "right");
            fail();
        } catch (RuntimeException caught) {
            assertThat(caught, is(taskFailed));
        }

        verify(function).apply("left", "right");
    }
}