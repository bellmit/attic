package com.loginbox.app.directory.bootstrap;

import com.loginbox.app.directory.Directories;
import com.loginbox.app.directory.internal.InternalDirectory;
import com.loginbox.app.directory.internal.InternalDirectoryQueries;
import com.loginbox.app.identity.User;
import com.loginbox.app.identity.UserInfo;
import com.loginbox.app.password.PasswordValidator;
import com.loginbox.transactor.transactable.Transform;
import org.apache.ibatis.session.SqlSession;

public abstract class Bootstrap {
    private final Gatekeeper setupGatekeeper;

    public Bootstrap(Gatekeeper setupGatekeeper) {
        this.setupGatekeeper = setupGatekeeper;
    }

    public Transform<SqlSession, UserInfo, User> setupAction() {
        PasswordValidator passwordValidator = getPasswordValidator();

        Transform<SqlSession, UserInfo, User> setupAction = (session, userInfo) -> {
            UserInfo digestedUserInfo = userInfo.withDigestedPassword(passwordValidator);

            Directories directories = getDirectories();
            InternalDirectory internalDirectory = directories.createInternalDirectory(session);

            User user = session
                    .getMapper(InternalDirectoryQueries.class)
                    .insertUser(internalDirectory, digestedUserInfo);

            return user;
        };
        return setupAction
                .andThen(setupGatekeeper.bootstrapCompletedAction());
    }

    protected abstract PasswordValidator getPasswordValidator();

    protected abstract Directories getDirectories();
}
