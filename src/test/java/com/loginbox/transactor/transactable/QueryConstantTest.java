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

public class QueryConstantTest {
    private final Context context = mock(Context.class);

    private final Query<Context, String> constant = Query.constant("result");

    @Test
    public void executesFunction() throws Exception {
        assertThat(constant.fetch(context), is("result"));
    }
}