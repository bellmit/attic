package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import com.loginbox.transactor.Transactables;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

public class QueryCombinatorTest {
    private final Transactables a = mock(Transactables.class);
    private final Transactables b = mock(Transactables.class);

    private final Context context = mock(Context.class);
    private RuntimeException failure = new RuntimeException("failed");

    @Test
    public void andThenAction() throws Exception {
        Query<Context, String> a = this.a::query;
        Action<Context> b = this.b::action;

        doReturn("result").when(this.a).query(context);

        Query<Context, String> chain = a.andThen(b);

        assertThat(chain.fetch(context), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).query(context);
        chainOrder.verify(this.b).action(context);
    }

    @Test
    public void transformedByTransform() throws Exception {
        Query<Context, String> a = this.a::query;
        Transform<Context, String, String> b = this.b::transform;

        doReturn("interim").when(this.a).query(context);
        doReturn("result").when(this.b).transform(context, "interim");

        Query<Context, String> chain = a.transformedBy(b);

        assertThat(chain.fetch(context), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).query(context);
        chainOrder.verify(this.b).transform(context, "interim");
    }

    @Test
    public void consumedBySink() throws Exception {
        Query<Context, String> a = this.a::query;
        Sink<Context, String> b = this.b::sink;

        doReturn("interim").when(this.a).query(context);

        Action<Context> chain = a.consumedBy(b);

        chain.execute(context);

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).query(context);
        chainOrder.verify(this.b).sink(context, "interim");
    }

    @Test
    public void intoLeftMerge() throws Exception {
        Query<Context, String> a = this.a::query;
        Merge<Context, String, String, String> b = this.b::merge;

        doReturn("interim").when(this.a).query(context);
        doReturn("result").when(this.b).merge(context, "interim", "value");

        Transform<Context, String, String> chain = a.intoLeft(b);

        assertThat(chain.apply(context, "value"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).query(context);
        chainOrder.verify(this.b).merge(context, "interim", "value");
    }

    @Test
    public void intoRightMerge() throws Exception {
        Query<Context, String> a = this.a::query;
        Merge<Context, String, String, String> b = this.b::merge;

        doReturn("interim").when(this.a).query(context);
        doReturn("result").when(this.b).merge(context, "value", "interim");

        Transform<Context, String, String> chain = a.intoRight(b);

        assertThat(chain.apply(context, "value"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).query(context);
        chainOrder.verify(this.b).merge(context, "value", "interim");
    }
}
