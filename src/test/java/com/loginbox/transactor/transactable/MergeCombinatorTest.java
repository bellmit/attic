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

public class MergeCombinatorTest {
    private final Transactables a = mock(Transactables.class);
    private final Transactables b = mock(Transactables.class);

    private final Context context = mock(Context.class);
    private RuntimeException failure = new RuntimeException("failed");

    @Test
    public void andThenAction() throws Exception {
        Merge<Context, String, String, String> a = this.a::merge;
        Action<Context> b = this.b::action;

        doReturn("result").when(this.a).merge(context, "left", "right");

        Merge<Context, String, String, String> chain = a.andThen(b);

        assertThat(chain.merge(context, "left", "right"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).merge(context, "left", "right");
        chainOrder.verify(this.b).action(context);
    }

    @Test
    public void transformedByTransform() throws Exception {
        Merge<Context, String, String, String> a = this.a::merge;
        Transform<Context, String, String> b = this.b::transform;

        doReturn("interim").when(this.a).merge(context, "left", "right");
        doReturn("result").when(this.b).transform(context, "interim");

        Merge<Context, String, String, String> chain = a.transformedBy(b);

        assertThat(chain.merge(context, "left", "right"), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).merge(context, "left", "right");
        chainOrder.verify(this.b).transform(context, "interim");
    }
}
