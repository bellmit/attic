package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TransformLiftTest {
    private final Context context = mock(Context.class);

    @SuppressWarnings("unchecked")
    private final Function<String, String> function = mock(Function.class);
    private final Transform<Context, String, String> lifted = Transform.lift(function);

    @Test
    public void executesFunction() throws Exception {
        doReturn("result").when(function).apply("value");

        assertThat(lifted.apply(context, "value"), is("result"));

        verify(function).apply("value");
    }

    @Test
    public void propagatesFailures() throws Exception {
        RuntimeException taskFailed = new RuntimeException("function failed");
        doThrow(taskFailed).when(function).apply("value");

        try {
            lifted.apply(context, "value");
            fail();
        } catch (RuntimeException caught) {
            assertThat(caught, is(taskFailed));
        }

        verify(function).apply("value");
    }
}