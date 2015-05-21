package com.loginbox.transactor.transactable;

import com.loginbox.transactor.Context;
import com.loginbox.transactor.Transactables;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class TransformCombinatorTest {
    private final Transactables a = mock(Transactables.class);
    private final Transactables b = mock(Transactables.class);

    private final Context context = mock(Context.class);
    private RuntimeException failure = new RuntimeException("failed");

    @Test
    public void andThenAction() throws Exception {
        Transform<Context, String, String> a = this.a::transform;
        Action<Context> b = this.b::action;

        doReturn("result").when(this.a).transform(context, "input");

        Transform<Context, String, String> chain = a.andThen(b);

        assertThat(chain.apply(context, "input"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).transform(context, "input");
        chainOrder.verify(this.b).action(context);
    }

    @Test
    public void transformedByTransform() throws Exception {
        Transform<Context, String, String> a = this.a::transform;
        Transform<Context, String, String> b = this.b::transform;

        doReturn("interim").when(this.a).transform(context, "input");
        doReturn("result").when(this.b).transform(context, "interim");

        Transform<Context, String, String> chain = a.transformedBy(b);

        assertThat(chain.apply(context, "input"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).transform(context, "input");
        chainOrder.verify(this.b).transform(context, "interim");
    }

    @Test
    public void consumedBySink() throws Exception {
        Transform<Context, String, String> a = this.a::transform;
        Sink<Context, String> b = this.b::sink;

        doReturn("interim").when(this.a).transform(context, "input");

        Sink<Context, String> chain = a.consumedBy(b);

        chain.consume(context, "input");

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).transform(context, "input");
        chainOrder.verify(this.b).sink(context, "interim");
    }

    @Test
    public void intoLeftMerge() throws Exception {
        Transform<Context, String, String> a = this.a::transform;
        Merge<Context, String, String, String> b = this.b::merge;

        doReturn("interim").when(this.a).transform(context, "left");
        doReturn("result").when(this.b).merge(context, "interim", "right");

        Merge<Context, String, String, String> chain = a.intoLeft(b);

        assertThat(chain.merge(context, "left", "right"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).transform(context, "left");
        chainOrder.verify(this.b).merge(context, "interim", "right");
    }

    @Test
    public void intoRightMerge() throws Exception {
        Transform<Context, String, String> a = this.a::transform;
        Merge<Context, String, String, String> b = this.b::merge;

        doReturn("interim").when(this.a).transform(context, "right");
        doReturn("result").when(this.b).merge(context, "left", "interim");

        Merge<Context, String, String, String> chain = a.intoRight(b);

        assertThat(chain.merge(context, "left", "right"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).transform(context, "right");
        chainOrder.verify(this.b).merge(context, "left", "interim");
    }
}
