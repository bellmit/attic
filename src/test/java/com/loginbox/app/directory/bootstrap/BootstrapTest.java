package com.loginbox.app.directory.bootstrap;

import com.loginbox.app.directory.Directories;
import com.loginbox.app.directory.DirectoryRepository;
import com.loginbox.app.directory.internal.InternalDirectory;
import com.loginbox.app.directory.internal.InternalDirectoryConfiguration;
import com.loginbox.app.directory.internal.InternalDirectoryQueries;
import com.loginbox.app.identity.User;
import com.loginbox.app.identity.UserInfo;
import com.loginbox.app.password.PasswordValidator;
import com.loginbox.app.transactor.mybatis.TransactorTestCase;
import com.loginbox.transactor.Transactor;
import com.loginbox.transactor.transactable.Transform;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BootstrapTest extends TransactorTestCase {
    private final Directories directories = new Directories();
    private final PasswordValidator passwordValidator = mock(PasswordValidator.class);
    @SuppressWarnings("unchecked")
    private final Transactor<SqlSession> setupTransactor = mock(Transactor.class);
    private final Gatekeeper setupGatekeeper = new Gatekeeper(setupTransactor);

    private final Bootstrap bootstrap
            = new Bootstrap(directories, setupGatekeeper, passwordValidator);

    private final GatekeeperRepository gatekeeperRepository = mockMapper(GatekeeperRepository.class);
    private final InternalDirectoryQueries queries = mockMapper(InternalDirectoryQueries.class);
    private final DirectoryRepository directoryRepository = mockMapper(DirectoryRepository.class);

    private final UUID directoryId = UUID.randomUUID();
    private final InternalDirectoryConfiguration newDirectoryConfig = mock(InternalDirectoryConfiguration.class);

    private final UserInfo userInfo = mock(UserInfo.class);
    private final UserInfo digestedUserInfo = mock(UserInfo.class);

    private final User newUser = mock(User.class);

    @Before
    public void wireMocks() {
        when(directoryRepository.insertInternalDirectory()).thenReturn(newDirectoryConfig);
        when(newDirectoryConfig.getId()).thenReturn(directoryId);

        when(userInfo.withDigestedPassword(passwordValidator)).thenReturn(digestedUserInfo);

        when(queries.insertUser(any(InternalDirectory.class), refEq(digestedUserInfo))).thenReturn(newUser);
    }

    @Test
    public void createsUser() throws Exception {
        Transform<SqlSession, UserInfo, User> setupAction = bootstrap.setupAction();
        User result = setupAction.apply(sqlSession, userInfo);

        assertThat(result, is(newUser));

        verify(queries).insertUser(any(InternalDirectory.class), refEq(digestedUserInfo));
        verify(gatekeeperRepository).bootstrapCompleted();
    }
}