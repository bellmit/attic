package com.loginbox.transactor;

import com.loginbox.transactor.transactable.Merge;
import org.junit.Before;

import java.util.UUID;

import static org.mockito.Mockito.when;

public class TransactorMergeTest extends TransactorTestCase {
    private final Merge<Context, String, String, String> transform = transactables::merge;

    private final String left = UUID.randomUUID().toString();
    private final String right = UUID.randomUUID().toString();

    @Before
    public void transformResult() throws Exception {
        when(transactables.merge(context, left, right)).thenReturn(successfulResult);
    }

    @Override
    protected String execute() throws Exception {
        return transactor.combine(transform, left, right);
    }

    @Override
    protected void establishExpectationsOn(Transactables transactables) throws Exception {
        transactables.merge(context, left, right);
    }
}
