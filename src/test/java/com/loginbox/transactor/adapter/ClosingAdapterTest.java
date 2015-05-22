package com.loginbox.transactor.adapter;

import com.loginbox.transactor.Context;
import com.loginbox.transactor.Transactables;
import com.loginbox.transactor.transactable.Action;
import com.loginbox.transactor.transactable.Merge;
import com.loginbox.transactor.transactable.Query;
import com.loginbox.transactor.transactable.Sink;
import com.loginbox.transactor.transactable.Transform;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClosingAdapterTest {
    private final Transactables transactables = mock(Transactables.class);
    private final Adapters adapters = mock(Adapters.class);

    private final Context context = mock(Context.class);
    private final int integerContext = new Random().nextInt();

    private final ClosingAdapter<Integer, Context> adapter = adapters::adapt;

    @Before
    public void configureAdapters() {
        when(adapters.adapt(integerContext)).thenReturn(context);
    }

    @Test
    public void wrapsAction() throws Exception {
        Action<Context> action = transactables::action;
        Action<Integer> wrapped = adapter.around(action);

        wrapped.execute(integerContext);

        InOrder closeOrder = inOrder(transactables, context);
        closeOrder.verify(transactables).action(context);
        closeOrder.verify(context).close();
    }

    @Test
    public void wrapsSink() throws Exception {
        Sink<Context, String> sink = transactables::sink;
        Sink<Integer, String> wrapped = adapter.around(sink);

        wrapped.consume(integerContext, "value");

        InOrder closeOrder = inOrder(transactables, context);
        closeOrder.verify(transactables).sink(context, "value");
        closeOrder.verify(context).close();
    }

    @Test
    public void wrapsQuery() throws Exception {
        Query<Context, String> query = transactables::query;
        Query<Integer, String> wrapped = adapter.around(query);

        doReturn("result").when(transactables).query(context);

        assertThat(wrapped.fetch(integerContext), is("result"));

        InOrder closeOrder = inOrder(transactables, context);
        closeOrder.verify(transactables).query(context);
        closeOrder.verify(context).close();
    }

    @Test
    public void wrapsTransform() throws Exception {
        Transform<Context, String, String> transform = transactables::transform;
        Transform<Integer, String, String> wrapped = adapter.around(transform);

        doReturn("result").when(transactables).transform(context, "value");

        assertThat(wrapped.apply(integerContext, "value"), is("result"));

        InOrder closeOrder = inOrder(transactables, context);
        closeOrder.verify(transactables).transform(context, "value");
        closeOrder.verify(context).close();
    }

    @Test
    public void wrapsMerge() throws Exception {
        Merge<Context, String, String, String> merge = transactables::merge;
        Merge<Integer, String, String, String> wrapped = adapter.around(merge);

        doReturn("result").when(transactables).merge(context, "left", "right");

        assertThat(wrapped.merge(integerContext, "left", "right"), is("result"));

        InOrder closeOrder = inOrder(transactables, context);
        closeOrder.verify(transactables).merge(context, "left", "right");
        closeOrder.verify(context).close();
    }
}