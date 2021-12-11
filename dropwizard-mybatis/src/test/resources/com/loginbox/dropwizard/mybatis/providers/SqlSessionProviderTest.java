package com.loginbox.dropwizard.mybatis.providers;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SqlSessionProviderTest {
    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);
    private final SqlSessionProvider provider = new SqlSessionProvider(sqlSessionFactory);

    private final SqlSession sqlSession = mock(SqlSession.class);

    @Before
    public void wireMocks() {
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
    }

    @Test
    public void providesSessionFromFactory() {
        assertThat(provider.provide(), is(sqlSession));
    }

    @Test
    public void disposeClosesSession() {
        provider.dispose(sqlSession);

        verify(sqlSession).close();
    }
}
