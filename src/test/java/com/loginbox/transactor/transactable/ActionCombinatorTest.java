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

public class ActionCombinatorTest {
    private final Transactables a = mock(Transactables.class);
    private final Transactables b = mock(Transactables.class);

    private final Context context = mock(Context.class);
    private RuntimeException failure = new RuntimeException("failed");

    @Test
    public void andThenAction() throws Exception {
        Action<Context> a = this.a::action;
        Action<Context> b = this.b::action;

        Action<Context> chain = a.andThen(b);

        chain.execute(context);

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).action(context);
        chainOrder.verify(this.b).action(context);
    }

    @Test
    public void beforeQuery() throws Exception {
        Action<Context> a = this.a::action;
        Query<Context, String> b = this.b::query;

        doReturn("result").when(this.b).query(context);

        Query<Context, String> chain = a.before(b);

        assertThat(chain.fetch(context), is("result"));

        InOrder chainOrder = inOrder(this.a, this.b);
        chainOrder.verify(this.a).action(context);
        chainOrder.verify(this.b).query(context);
    }
}
