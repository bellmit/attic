package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import com.loginbox.transactor.Transactables;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransformPivotTest {
    private final Transactables transactables = mock(Transactables.class);

    private final Context context = mock(Context.class);

    private final Transform<Context, String, String> original = transactables::transform;
    private final Transform<String, Context, String> pivoted = original.pivot();

    @Test
    public void executesFunction() throws Exception {
        doReturn("result").when(transactables).transform(context, "value");

        assertThat(pivoted.apply("value", context), is("result"));

        verify(transactables).transform(context, "value");
    }

    @Test
    public void propagatesFailures() throws Exception {
        RuntimeException taskFailed = new RuntimeException("transform failed");
        doThrow(taskFailed).when(transactables).transform(context, "value");

        try {
            pivoted.apply("value", context);
            fail();
        } catch (RuntimeException caught) {
            assertThat(caught, is(taskFailed));
        }

        verify(transactables).transform(context, "value");
    }
}