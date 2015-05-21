package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import com.loginbox.transactor.Transactables;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

public class SinkCombinatorTest {
    private final Transactables a = mock(Transactables.class);
    private final Transactables b = mock(Transactables.class);

    private final Context context = mock(Context.class);
    private RuntimeException failure = new RuntimeException("failed");

    @Test
    public void andThenAction() throws Exception {
        Sink<Context, String> a = this.a::sink;
        Action<Context> b = this.b::action;

        Sink<Context, String> chain = a.andThen(b);

        chain.consume(context, "value");

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).sink(context, "value");
        chainOrder.verify(this.b).action(context);
    }

    @Test
    public void beforeQuery() throws Exception {
        Sink<Context, String> a = this.a::sink;
        Query<Context, String> b = this.b::query;

        doReturn("result").when(this.b).query(context);

        Transform<Context, String, String> chain = a.before(b);

        assertThat(chain.apply(context, "input"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).sink(context, "input");
        chainOrder.verify(this.b).query(context);
    }
}
