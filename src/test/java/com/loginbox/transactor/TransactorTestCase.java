package com.loginbox.transactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public abstract class TransactorTestCase {
    protected final Transactables transactables = mock(Transactables.class);

    @SuppressWarnings("unchecked")
    protected final Transactor<Context> transactor = spy(Transactor.class);

    protected final Context context = mock(Context.class);

    protected final String successfulResult = UUID.randomUUID().toString();

    @Before
    public void wireMocks() throws Exception {
        when(transactor.createContext()).thenReturn(context);
    }

    @Test
    public void successful() throws Exception {
        assertThat(execute(), is(successfulResult));

        verifyFinished();
    }

    @Test
    public void failedInTransactable() throws Exception {
        Exception transactableFailed = transactableFails();

        try {
            execute();
            fail();
        } catch (Exception caught) {
            assertThat(caught, is(transactableFailed));
        }

        verifyAborted();
    }

    @Test
    public void failedInFinish() throws Exception {
        Exception finishFailed = finishFails();

        try {
            execute();
            fail();
        } catch (Exception caught) {
            assertThat(caught, is(finishFailed));
        }

        verifyFinished();
    }

    @Test
    public void failedInAbort() throws Exception {
        Exception transactableFailed = transactableFails();
        Exception abortFailed = abortFails();

        try {
            execute();
            fail();
        } catch (Exception caught) {
            assertThat(caught, is(transactableFailed));
            assertThat(caught.getSuppressed(), hasItemInArray(abortFailed));
        }

        verifyAborted();
    }

    @Test
    public void failedInClose() throws Exception {
        Exception closeFailed = closeFails();

        try {
            execute();
            fail();
        } catch (Exception caught) {
            assertThat(caught, is(closeFailed));
        }

        verifyFinished();
    }

    @Test
    public void failedInFinishAndClose() throws Exception {
        Exception finishFailed = finishFails();
        Exception closeFailed = closeFails();

        try {
            execute();
            fail();
        } catch (Exception caught) {
            assertThat(caught, is(finishFailed));
            assertThat(caught.getSuppressed(), hasItemInArray(closeFailed));
        }

        verifyFinished();
    }

    @Test
    public void failedInTransactableAndClose() throws Exception {
        Exception transactableFailed = transactableFails();
        Exception closeFailed = closeFails();

        try {
            execute();
            fail();
        } catch (Exception caught) {
            assertThat(caught, is(transactableFailed));
            assertThat(caught.getSuppressed(), hasItemInArray(closeFailed));
        }

        verifyAborted();
    }

    @Test
    public void failedInTransactableAndAbortAndClose() throws Exception {
        /* some tests just want to watch the world burn */
        Exception transactableFailed = transactableFails();
        Exception abortFailed = abortFails();
        Exception closeFailed = closeFails();

        try {
            execute();
            fail();
        } catch (Exception caught) {
            assertThat(caught, is(transactableFailed));
            assertThat(caught.getSuppressed(), hasItemInArray(abortFailed));
            assertThat(caught.getSuppressed(), hasItemInArray(closeFailed));
        }

        verifyAborted();
    }

    protected abstract String execute() throws Exception;

    protected abstract void establishExpectationsOn(Transactables transactables) throws Exception;

    private Exception transactableFails() throws Exception {
        Exception transactableFailed = new Exception("transactable failed");
        establishExpectationsOn(doThrow(transactableFailed).when(transactables));
        return transactableFailed;
    }

    private Exception finishFails() throws Exception {
        Exception finishFailed = new Exception("finish failed");
        doThrow(finishFailed).when(transactor).finish(context);
        return finishFailed;
    }

    private Exception abortFails() throws Exception {
        Exception abortFailed = new Exception("abort failed");
        doThrow(abortFailed).when(transactor).abort(context);
        return abortFailed;
    }

    private Exception closeFails() throws Exception {
        Exception closeFailed = new Exception("close failed");
        doThrow(closeFailed).when(context).close();
        return closeFailed;
    }

    private void verifyFinished() throws Exception {
        InOrder transaction = inOrder(transactables, transactor, context);
        establishExpectationsOn(transaction.verify(transactables));
        transaction.verify(transactor).finish(context);
        transaction.verify(context).close();
    }

    private void verifyAborted() throws Exception {
        InOrder transaction = inOrder(transactables, transactor, context);
        establishExpectationsOn(transaction.verify(transactables));
        transaction.verify(transactor).abort(context);
        transaction.verify(context).close();
    }
}