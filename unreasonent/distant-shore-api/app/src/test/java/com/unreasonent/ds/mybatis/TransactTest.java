package com.unreasonent.ds.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.unreasonent.ds.mybatis.Transact.transact;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

public class TransactTest {
    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);
    private final SqlSession sqlSession = mock(SqlSession.class);

    @SuppressWarnings("unchecked")
    private final Consumer<SqlSession> consumer = mock(Consumer.class);
    @SuppressWarnings("unchecked")
    private final Function<SqlSession, String> function = mock(Function.class);

    @Before
    public void wireMocks() {
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(function.apply(sqlSession)).thenReturn("SUCCESS");
    }

    @Test
    public void consumer() {
        transact(sqlSessionFactory, consumer);

        InOrder inOrder = inOrder(sqlSessionFactory, consumer, sqlSession);
        inOrder.verify(sqlSessionFactory).openSession();
        inOrder.verify(consumer).accept(sqlSession);
        inOrder.verify(sqlSession).commit();
        inOrder.verify(sqlSession).close();
    }

    @Test
    public void function() {
        assertThat(Transact.transacted(sqlSessionFactory, function), equalTo("SUCCESS"));

        InOrder inOrder = inOrder(sqlSessionFactory, function, sqlSession);
        inOrder.verify(sqlSessionFactory).openSession();
        inOrder.verify(function).apply(sqlSession);
        inOrder.verify(sqlSession).commit();
        inOrder.verify(sqlSession).close();
    }

    @Test
    public void consumerFails() {
        Throwable error = new RuntimeException("foo");
        doThrow(error).when(consumer).accept(sqlSession);

        try {
            transact(sqlSessionFactory, consumer);
            fail();
        } catch (RuntimeException expected) {
            assertThat(expected, equalTo(error));
        }

        InOrder inOrder = inOrder(sqlSessionFactory, consumer, sqlSession);
        inOrder.verify(sqlSessionFactory).openSession();
        inOrder.verify(consumer).accept(sqlSession);
        inOrder.verify(sqlSession, never()).commit();
        inOrder.verify(sqlSession).close();
    }

    @Test
    public void functionFails() {
        Throwable error = new RuntimeException("foo");
        doThrow(error).when(function).apply(sqlSession);

        try {
            Transact.transacted(sqlSessionFactory, function);
            fail();
        } catch (RuntimeException expected) {
            assertThat(expected, equalTo(error));
        }

        InOrder inOrder = inOrder(sqlSessionFactory, function, sqlSession);
        inOrder.verify(sqlSessionFactory).openSession();
        inOrder.verify(function).apply(sqlSession);
        inOrder.verify(sqlSession, never()).commit();
        inOrder.verify(sqlSession).close();
    }
}