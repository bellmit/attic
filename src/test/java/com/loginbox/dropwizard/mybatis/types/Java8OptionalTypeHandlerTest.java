package com.loginbox.dropwizard.mybatis.types;

import org.apache.ibatis.type.JdbcType;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Java8OptionalTypeHandlerTest {
    private final Java8OptionalTypeHandler handler = new Java8OptionalTypeHandler();

    @Test
    public void setsValue() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        int index = 5;
        Optional<Object> value = Optional.of("Hello");
        JdbcType jdbcType = JdbcType.VARCHAR;

        handler.setParameter(ps, index, value, jdbcType);

        verify(ps).setObject(index, "Hello", Types.VARCHAR);
    }

    @Test
    public void setsEmpty() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        int index = 5;
        Optional<Object> value = Optional.empty();
        JdbcType jdbcType = JdbcType.VARCHAR;

        handler.setParameter(ps, index, value, jdbcType);

        verify(ps).setNull(index, Types.VARCHAR);
    }

    @Test
    public void setsNull() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        int index = 5;
        Optional<Object> value = null;
        JdbcType jdbcType = JdbcType.VARCHAR;

        handler.setParameter(ps, index, value, jdbcType);

        verify(ps).setNull(index, Types.VARCHAR);
    }

    @Test
    public void readsNonNullResultByIndex() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        int index = 5;
        Optional<Object> expected = Optional.of("Hello");

        when(rs.getObject(index)).thenReturn("Hello");

        Optional<Object> result = handler.getResult(rs, index);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNullResultByIndex() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        int index = 5;
        Optional<Object> expected = Optional.empty();

        when(rs.getObject(index)).thenReturn(null);
        when(rs.wasNull()).thenReturn(true);

        Optional<Object> result = handler.getResult(rs, index);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNonNullResultByName() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        String column = "column name";
        Optional<Object> expected = Optional.of("Hello");

        when(rs.getObject(column)).thenReturn("Hello");

        Optional<Object> result = handler.getResult(rs, column);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNullResultByName() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        String column = "column name";
        Optional<Object> expected = Optional.empty();

        when(rs.getObject(column)).thenReturn(null);
        when(rs.wasNull()).thenReturn(true);

        Optional<Object> result = handler.getResult(rs, column);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNonNullCallableResult() throws SQLException {
        CallableStatement cs = mock(CallableStatement.class);
        int index = 5;
        Optional<Object> expected = Optional.of("Hello");

        when(cs.getObject(index)).thenReturn("Hello");

        Optional<Object> result = handler.getResult(cs, index);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNullCallableResult() throws SQLException {
        CallableStatement cs = mock(CallableStatement.class);
        int index = 5;
        Optional<Object> expected = Optional.empty();

        when(cs.getObject(index)).thenReturn(null);
        when(cs.wasNull()).thenReturn(true);

        Optional<Object> result = handler.getResult(cs, index);

        assertThat(result, is(expected));
    }
}