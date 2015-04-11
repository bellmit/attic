package com.loginbox.dropwizard.mybatis.types;

import org.apache.ibatis.type.JdbcType;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UuidTypeHandlerTest {
    private final UuidTypeHandler handler = new UuidTypeHandler();

    @Test
    public void setsNonNullParameter() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        int index = 5;
        UUID uuid = UUID.randomUUID();
        JdbcType jdbcType = JdbcType.OTHER;

        handler.setParameter(ps, index, uuid, jdbcType);

        verify(ps).setObject(index, uuid);
    }

    @Test
    public void setsNullParameter() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        int index = 5;
        UUID uuid = null;
        JdbcType jdbcType = JdbcType.OTHER;

        handler.setParameter(ps, index, uuid, jdbcType);

        verify(ps).setNull(index, Types.OTHER);
    }

    @Test
    public void readsNonNullResultByIndex() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        int index = 5;
        UUID expected = UUID.randomUUID();

        when(rs.getObject(index)).thenReturn(expected);

        UUID result = handler.getResult(rs, index);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNullResultByIndex() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        int index = 5;
        UUID expected = null;

        when(rs.getObject(index)).thenReturn(expected);
        when(rs.wasNull()).thenReturn(true);

        UUID result = handler.getResult(rs, index);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNonNullResultByName() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        String column = "column name";
        UUID expected = UUID.randomUUID();

        when(rs.getObject(column)).thenReturn(expected);

        UUID result = handler.getResult(rs, column);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNullResultByName() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        String column = "column name";
        UUID expected = null;

        when(rs.getObject(column)).thenReturn(expected);
        when(rs.wasNull()).thenReturn(true);

        UUID result = handler.getResult(rs, column);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNonNullCallableResult() throws SQLException {
        CallableStatement cs = mock(CallableStatement.class);
        int index = 5;
        UUID expected = UUID.randomUUID();

        when(cs.getObject(index)).thenReturn(expected);

        UUID result = handler.getResult(cs, index);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNullCallableResult() throws SQLException {
        CallableStatement cs = mock(CallableStatement.class);
        int index = 5;
        UUID expected = null;

        when(cs.getObject(index)).thenReturn(null);
        when(cs.wasNull()).thenReturn(true);

        UUID result = handler.getResult(cs, index);

        assertThat(result, is(expected));
    }
}