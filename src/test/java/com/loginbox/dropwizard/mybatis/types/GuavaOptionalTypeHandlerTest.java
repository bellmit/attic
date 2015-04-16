package com.loginbox.dropwizard.mybatis.types;

import com.google.common.base.Optional;
import org.apache.ibatis.type.JdbcType;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GuavaOptionalTypeHandlerTest {
    private final GuavaOptionalTypeHandler handler = new GuavaOptionalTypeHandler();

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
        Optional<Object> expected = Optional.absent();

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
        Optional<Object> expected = Optional.absent();

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
        Optional<Object> expected = Optional.absent();

        when(cs.getObject(index)).thenReturn(null);
        when(cs.wasNull()).thenReturn(true);

        Optional<Object> result = handler.getResult(cs, index);

        assertThat(result, is(expected));
    }
}