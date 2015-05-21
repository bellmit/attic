package com.loginbox.transactor;

import com.loginbox.transactor.transactable.Action;

public class TransactorActionTest extends TransactorTestCase {
    private final Action<Context> action = transactables::action;

    @Override
    protected String execute() throws Exception {
        transactor.execute(action);
        return successfulResult;
    }

    @Override
    protected void establishExpectationsOn(Transactables transactables) throws Exception {
        transactables.action(context);
    }
}
