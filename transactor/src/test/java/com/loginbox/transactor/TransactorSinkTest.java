package com.loginbox.transactor;

import com.loginbox.transactor.transactable.Sink;

import java.util.UUID;

public class TransactorSinkTest extends TransactorTestCase {
    private final Sink<Context, String> sink = transactables::sink;

    private final String value = UUID.randomUUID().toString();

    @Override
    protected String execute() throws Exception {
        transactor.consume(sink, value);
        return successfulResult;
    }

    @Override
    protected void establishExpectationsOn(Transactables transactables) throws Exception {
        transactables.sink(context, value);
    }
}
