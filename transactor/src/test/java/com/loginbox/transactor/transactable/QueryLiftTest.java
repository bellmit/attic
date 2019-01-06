package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import org.junit.Test;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class QueryLiftTest {
    private final Context context = mock(Context.class);

    @SuppressWarnings("unchecked")
    private final Supplier<String> supplier = mock(Supplier.class);
    private final Query<Context, String> lifted = Query.lift(supplier);

    @Test
    public void executesFunction() throws Exception {
        doReturn("result").when(supplier).get();

        assertThat(lifted.fetch(context), is("result"));

        verify(supplier).get();
    }

    @Test
    public void propagatesFailures() throws Exception {
        RuntimeException taskFailed = new RuntimeException("supplier failed");
        doThrow(taskFailed).when(supplier).get();

        try {
            lifted.fetch(context);
            fail();
        } catch (RuntimeException caught) {
            assertThat(caught, is(taskFailed));
        }

        verify(supplier).get();
    }
}