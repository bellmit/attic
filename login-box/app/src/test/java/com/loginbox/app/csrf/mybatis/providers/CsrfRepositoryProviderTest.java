package com.loginbox.app.csrf.mybatis.providers;

import com.loginbox.app.csrf.CsrfToken;
import com.loginbox.app.csrf.storage.CsrfRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsrfRepositoryProviderTest {
    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);
    private final CsrfRepositoryProvider provider = new CsrfRepositoryProvider(() -> sqlSessionFactory);

    private final SqlSession sqlSession = mock(SqlSession.class);
    private final CsrfRepository csrfRepository = mock(CsrfRepository.class);

    @Before
    public void wireMocks() {
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.getMapper(CsrfRepository.class)).thenReturn(csrfRepository);
    }

    @Test
    public void createsMapperToInject() {
        assertThat(provider.provide(), is(instanceOf(TransactedCsrfRepository.class)));
    }

    @Test
    public void mapperForwardsInserts() {
        TransactedCsrfRepository repo = provider.provide();

        CsrfToken csrfToken = new CsrfToken("TOKEN");
        repo.insertCsrfToken("SESSION", csrfToken);

        verify(csrfRepository).insertCsrfToken("SESSION", csrfToken);
    }

    @Test
    public void mapperForwardsConsumes() {
        TransactedCsrfRepository repo = provider.provide();

        CsrfToken csrfToken = new CsrfToken("TOKEN");
        repo.consumeToken("SESSION", csrfToken);

        verify(csrfRepository).consumeToken("SESSION", csrfToken);
    }

    @Test
    public void mapperForwardsExpires() {
        TransactedCsrfRepository repo = provider.provide();

        repo.expireTokens();

        verify(csrfRepository).expireTokens();
    }

    @Test
    public void mapperCommitsOnDispose() {
        TransactedCsrfRepository repo = provider.provide();
        provider.dispose(repo);

        InOrder cleanup = inOrder(sqlSession);
        cleanup.verify(sqlSession).commit();
        cleanup.verify(sqlSession).close();
    }

    @Test
    public void mapperCommitsOnDisposeOnException() {
        RuntimeException expected = new RuntimeException("foo!");
        doThrow(expected).when(sqlSession).commit();

        try {
            TransactedCsrfRepository repo = provider.provide();
            provider.dispose(repo);
            fail();
        } catch (RuntimeException thrown) {
            assertThat(thrown, is(expected));
        }

        InOrder cleanup = inOrder(sqlSession);
        cleanup.verify(sqlSession).commit();
        cleanup.verify(sqlSession).close();
    }
}
