package com.loginbox.transactor;

import com.loginbox.transactor.transactable.Transform;
import org.junit.Before;

import java.util.UUID;

import static org.mockito.Mockito.when;

public class TransactorTransformTest extends TransactorTestCase {
    private final Transform<Context, String, String> transform = transactables::transform;

    private final String value = UUID.randomUUID().toString();

    @Before
    public void transformResult() throws Exception {
        when(transactables.transform(context, value)).thenReturn(successfulResult);
    }

    @Override
    protected void establishExpectationsOn(Transactables transactables) throws Exception {
        transactables.transform(context, value);
    }

    @Override
    protected String execute() throws Exception {
        return transactor.apply(transform, value);
    }
}
