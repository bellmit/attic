package com.loginbox.app.directory;

import com.loginbox.app.directory.internal.InternalDirectory;
import com.loginbox.app.directory.internal.InternalDirectoryConfiguration;
import com.loginbox.app.transactor.mybatis.TransactorTestCase;
import com.loginbox.transactor.transactable.Query;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class DirectoriesTest extends TransactorTestCase {
    private final Directories directories = new Directories();

    private final DirectoryRepository directoryRepository = mockMapper(DirectoryRepository.class);

    @Test
    public void createInternalDirectory() throws Exception {
        UUID id = UUID.randomUUID();
        InternalDirectoryConfiguration directoryConfiguration = new InternalDirectoryConfiguration(id);
        when(directoryRepository.insertInternalDirectory()).thenReturn(directoryConfiguration);

        Query<SqlSession, InternalDirectory> createQuery = directories.createInternalDirectory();

        InternalDirectory directory = createQuery.fetch(sqlSession);

        assertThat(directory.getId(), is(id));
    }
}