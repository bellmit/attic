package com.loginbox.app.directory;

import com.loginbox.app.directory.internal.InternalDirectory;
import com.loginbox.app.directory.internal.InternalDirectoryConfiguration;
import com.loginbox.transactor.transactable.Query;
import com.loginbox.transactor.transactable.Transform;
import org.apache.ibatis.session.SqlSession;

import static com.loginbox.app.transactor.mybatis.MybatisAdapters.mapper;
import static com.loginbox.transactor.transactable.Transform.lift;

public class Directories {
    public Query<SqlSession, InternalDirectory> createInternalDirectory() {
        Query<SqlSession, InternalDirectoryConfiguration> insertStep
                = mapper(DirectoryRepository.class)
                .around(DirectoryRepository::insertInternalDirectory);

        Transform<SqlSession, InternalDirectoryConfiguration, InternalDirectory> configureStep
                = lift(this::configureInternalDirectory);

        return insertStep
                .transformedBy(configureStep);
    }

    private InternalDirectory configureInternalDirectory(InternalDirectoryConfiguration configuration) {
        return new InternalDirectory(configuration);
    }
}
