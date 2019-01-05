package com.loginbox.app.transactor.mybatis;

import org.apache.ibatis.session.SqlSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactorTestCase {
    protected final SqlSession sqlSession = mock(SqlSession.class);

    protected <T> T mockMapper(Class<T> mapperInterface) {
        T mock = mock(mapperInterface);
        when(sqlSession.getMapper(mapperInterface)).thenReturn(mock);
        return mock;
    }
}
