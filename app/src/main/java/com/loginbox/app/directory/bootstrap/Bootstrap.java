package com.loginbox.app.directory.bootstrap;

import com.loginbox.app.directory.Directories;
import com.loginbox.app.directory.internal.InternalDirectory;
import com.loginbox.app.directory.internal.InternalDirectoryQueries;
import com.loginbox.app.identity.User;
import com.loginbox.app.identity.UserInfo;
import com.loginbox.app.password.PasswordValidator;
import com.loginbox.transactor.transactable.Action;
import com.loginbox.transactor.transactable.Merge;
import com.loginbox.transactor.transactable.Query;
import com.loginbox.transactor.transactable.Transform;
import org.apache.ibatis.session.SqlSession;

import static com.loginbox.app.transactor.mybatis.MybatisAdapters.mapper;

public abstract class Bootstrap {
    private final Directories directories;
    private final Gatekeeper setupGatekeeper;

    public Bootstrap(
            Directories directories, Gatekeeper setupGatekeeper) {
        this.directories = directories;
        this.setupGatekeeper = setupGatekeeper;
    }

    public Transform<SqlSession, UserInfo, User> setupAction() {
        Query<SqlSession, InternalDirectory> createInternalDirectoryAction
                = directories.createInternalDirectory();

        Query<SqlSession, PasswordValidator> passwordValidator
                = Query.lift(this::getPasswordValidator);
        Merge<SqlSession, UserInfo, PasswordValidator, UserInfo> passwordDigestWithValidator
                = Merge.lift(UserInfo::withDigestedPassword);
        Transform<SqlSession, UserInfo, UserInfo> passwordDigest
                = passwordValidator.intoRight(passwordDigestWithValidator);

        Merge<SqlSession, InternalDirectory, UserInfo, User> bootstrapUserAction
                = mapper(InternalDirectoryQueries.class)
                .around(InternalDirectoryQueries::insertUser);

        Merge<SqlSession, InternalDirectory, UserInfo, User> bootstrapUserWithDigestedPassword
                = passwordDigest.intoRight(bootstrapUserAction);

        Action<SqlSession> bootstrapCompletedAction
                = setupGatekeeper.bootstrapCompletedAction();

        return createInternalDirectoryAction
                .intoLeft(bootstrapUserWithDigestedPassword)
                .andThen(bootstrapCompletedAction);
    }

    protected abstract PasswordValidator getPasswordValidator();
}
