package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import org.junit.Test;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SinkLiftTest {
    private final Context context = mock(Context.class);

    @SuppressWarnings("unchecked")
    private final Consumer<String> consumer = mock(Consumer.class);
    private final Sink<Context, String> lifted = Sink.lift(consumer);

    @Test
    public void executesFunction() throws Exception {
        lifted.consume(context, "value");

        verify(consumer).accept("value");
    }

    @Test
    public void propagatesFailures() throws Exception {
        RuntimeException taskFailed = new RuntimeException("consumer failed");
        doThrow(taskFailed).when(consumer).accept("value");

        try {
            lifted.consume(context, "value");
            fail();
        } catch (RuntimeException caught) {
            assertThat(caught, is(taskFailed));
        }

        verify(consumer).accept("value");
    }
}