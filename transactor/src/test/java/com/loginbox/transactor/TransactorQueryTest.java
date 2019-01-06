package com.loginbox.transactor;

import com.loginbox.transactor.transactable.Query;
import org.junit.Before;

import static org.mockito.Mockito.when;

public class TransactorQueryTest extends TransactorTestCase {
    private final Query<Context, String> query = transactables::query;

    @Before
    public void queryResult() throws Exception {
        when(transactables.query(context)).thenReturn(successfulResult);
    }

    @Override
    protected String execute() throws Exception {
        return transactor.fetch(query);
    }

    @Override
    protected void establishExpectationsOn(Transactables transactables) throws Exception {
        transactables.query(context);
    }
}
