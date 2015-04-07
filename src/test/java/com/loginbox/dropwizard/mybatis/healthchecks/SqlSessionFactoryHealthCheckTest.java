package com.loginbox.dropwizard.mybatis.healthchecks;

import com.loginbox.dropwizard.mybatis.mappers.Ping;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import static com.loginbox.dropwizard.mybatis.healthchecks.matchers.HealthyResultMatcher.healthyResult;
import static com.loginbox.dropwizard.mybatis.healthchecks.matchers.UnhealthyResultMatcher.unhealthyResult;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlSessionFactoryHealthCheckTest {

    private final SqlSessionFactory sqlSessionFactory = mock(SqlSessionFactory.class);
    private final SqlSession session = mock(SqlSession.class);
    private final Ping mapper = mock(Ping.class);
    private final SqlSessionFactoryHealthCheck healthCheck = new SqlSessionFactoryHealthCheck(sqlSessionFactory);

    @Test
    public void healthy() throws Exception {
        when(sqlSessionFactory.openSession()).thenReturn(session);
        when(session.getMapper(Ping.class)).thenReturn(mapper);
        when(mapper.ping()).thenReturn(37);
        assertThat(healthCheck.check(), is(healthyResult()));
    }

    @Test
    public void unhealthyWhenOpenSessionFails() throws Exception {
        RuntimeException e = new RuntimeException();
        when(sqlSessionFactory.openSession()).thenThrow(e);
        assertThat(healthCheck.check(), is(unhealthyResult(e)));
    }

    @Test
    public void unhealthyWhenMapperAbsent() throws Exception {
        BindingException e = new BindingException();
        when(sqlSessionFactory.openSession()).thenReturn(session);
        when(session.getMapper(Ping.class)).thenThrow(e);
        assertThat(healthCheck.check(), is(unhealthyResult(e)));
    }

    @Test
    public void unhealthyWhenPingFails() throws Exception {
        RuntimeException e = new RuntimeException();
        when(sqlSessionFactory.openSession()).thenReturn(session);
        when(session.getMapper(Ping.class)).thenReturn(mapper);
        when(mapper.ping()).thenThrow(e);
        assertThat(healthCheck.check(), is(unhealthyResult(e)));
    }
}