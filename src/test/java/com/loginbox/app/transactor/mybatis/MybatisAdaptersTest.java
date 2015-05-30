package com.loginbox.app.transactor.mybatis;

import com.loginbox.transactor.adapter.Adapter;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MybatisAdaptersTest {

    private final SqlSession session = mock(SqlSession.class);

    @Test
    public void providesMapper() {
        Object mapper = new Object();
        when(session.getMapper(Object.class)).thenReturn(mapper);

        Adapter<SqlSession, Object> adapter = MybatisAdapters.mapper(Object.class);
        assertThat(adapter.convert(session), is(mapper));
    }
}