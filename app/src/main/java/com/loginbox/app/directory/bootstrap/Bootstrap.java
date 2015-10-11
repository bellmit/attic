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
    private final Gatekeeper setupGatekeeper;

    public Bootstrap(Gatekeeper setupGatekeeper) {
        this.setupGatekeeper = setupGatekeeper;
    }

    private static <C, I, O> Transform<I, C, O> pivot(Transform<? super C, ? super I, ? extends O> t) {
        return (context, input) -> t.apply(input, context);
    }

    public Transform<SqlSession, UserInfo, User> setupAction() {
        Query<SqlSession, PasswordValidator> passwordValidator
                = Query.lift(this::getPasswordValidator);

        Merge<SqlSession, UserInfo, PasswordValidator, UserInfo> digestUserInfoAgainstValidator
                = Merge.lift(UserInfo::withDigestedPassword);

        Transform<SqlSession, Directories, InternalDirectory> createInternalDirectoryInDirectories
                = pivot(Directories::createInternalDirectory);

        Merge<SqlSession, InternalDirectory, UserInfo, User> addInitialUserToDirectory
                = mapper(InternalDirectoryQueries.class)
                .around(InternalDirectoryQueries::insertUser);

        Action<SqlSession> bootstrapCompletedAction = setupGatekeeper.bootstrapCompletedAction();

        Query<SqlSession, Directories> directories = Query.lift(this::getDirectories);

        Query<SqlSession, InternalDirectory> createInternalDirectory
                = directories.transformedBy(createInternalDirectoryInDirectories);

        Transform<SqlSession, UserInfo, User> addInitialUserToNewDirectory
                = createInternalDirectory.intoLeft(addInitialUserToDirectory);

        Transform<SqlSession, UserInfo, UserInfo> digestUserInfo
                = passwordValidator.intoRight(digestUserInfoAgainstValidator);

        Transform<SqlSession, UserInfo, User> addDigestedInitialUserToNewDirectory
                = digestUserInfo.transformedBy(addInitialUserToNewDirectory);

        Transform<SqlSession, UserInfo, User> bootstrap = addDigestedInitialUserToNewDirectory
                .andThen(bootstrapCompletedAction);

        return bootstrap;
    }

    protected abstract PasswordValidator getPasswordValidator();

    protected abstract Directories getDirectories();
}
