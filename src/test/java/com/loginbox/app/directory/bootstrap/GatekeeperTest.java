package com.loginbox.app.directory.bootstrap;

import com.loginbox.transactor.Transactor;
import com.loginbox.transactor.transactable.Action;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GatekeeperTest {
    private final Transactor<SqlSession> transactor = new Transactor<SqlSession>() {
        @Override
        protected SqlSession createContext() throws Exception {
            return context;
        }
    };

    private final Gatekeeper gatekeeper = new Gatekeeper(transactor);

    private final SqlSession context = mock(SqlSession.class);
    private final GatekeeperRepository repository = mock(GatekeeperRepository.class);

    @Before
    public void wireMocks() throws Exception {
        when(context.getMapper(GatekeeperRepository.class)).thenReturn(repository);
    }

    @Test
    public void marksSetupInRepository() throws Exception {
        transactor.execute(gatekeeper.bootstrapCompletedAction());

        verify(repository).bootstrapCompleted();
    }
    @Test
    public void returnsSetupModeFromRepository() throws Exception {
        when(repository.isBootstrapped()).thenReturn(true);

        assertThat(gatekeeper.isBootstrapped(), is(true));
        verify(repository).isBootstrapped();
    }
}