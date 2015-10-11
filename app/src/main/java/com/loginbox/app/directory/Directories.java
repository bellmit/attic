package com.loginbox.app.directory;

import com.loginbox.app.directory.internal.InternalDirectory;
import com.loginbox.app.directory.internal.InternalDirectoryConfiguration;
import com.loginbox.transactor.transactable.Query;
import com.loginbox.transactor.transactable.Transform;
import org.apache.ibatis.session.SqlSession;

import static com.loginbox.app.transactor.mybatis.MybatisAdapters.mapper;

public class Directories {
    public InternalDirectory createInternalDirectory(SqlSession sqlSession) {
        DirectoryRepository repository = sqlSession.getMapper(DirectoryRepository.class);
        InternalDirectoryConfiguration configuration = repository.insertInternalDirectory();
        InternalDirectory directory = configureInternalDirectory(configuration);
        return directory;
    }

    private InternalDirectory configureInternalDirectory(InternalDirectoryConfiguration configuration) {
        return new InternalDirectory(configuration);
    }
}
