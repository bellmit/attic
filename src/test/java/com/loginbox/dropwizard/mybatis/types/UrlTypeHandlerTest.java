package com.loginbox.dropwizard.mybatis.types;

import org.apache.ibatis.type.JdbcType;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UrlTypeHandlerTest {
    private final UrlTypeHandler handler = new UrlTypeHandler();

    @Test
    public void setsNonNullParameter() throws SQLException, MalformedURLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        int index = 5;
        URL url = new URL("http://www.example.com/");
        JdbcType jdbcType = JdbcType.OTHER;

        handler.setParameter(ps, index, url, jdbcType);

        verify(ps).setString(index, "http://www.example.com/");
    }

    @Test
    public void setsNullParameter() throws SQLException {
        PreparedStatement ps = mock(PreparedStatement.class);
        int index = 5;
        URL url = null;
        JdbcType jdbcType = JdbcType.OTHER;

        handler.setParameter(ps, index, url, jdbcType);

        verify(ps).setNull(index, Types.OTHER);
    }

    @Test
    public void readsNonNullResultByIndex() throws SQLException, MalformedURLException {
        ResultSet rs = mock(ResultSet.class);
        int index = 5;
        URL expected = new URL("http://www.example.com/");

        when(rs.getString(index)).thenReturn("http://www.example.com/");

        URL result = handler.getResult(rs, index);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNullResultByIndex() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        int index = 5;

        when(rs.getString(index)).thenReturn(null);
        when(rs.wasNull()).thenReturn(true);

        URL result = handler.getResult(rs, index);

        assertThat(result, is(nullValue()));
    }

    @Test
    public void readsNonNullResultByName() throws SQLException, MalformedURLException {
        ResultSet rs = mock(ResultSet.class);
        String column = "column name";
        URL expected = new URL("http://www.example.com/");

        when(rs.getString(column)).thenReturn("http://www.example.com/");

        URL result = handler.getResult(rs, column);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNonUrlResultByIndex() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        int index = 5;

        when(rs.getString(index)).thenReturn("a banana");

        try{
            handler.getResult(rs, index);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getCause(), is(instanceOf(MalformedURLException.class)));
        }
    }

    @Test
    public void readsNonUrlResultByName() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        String column = "column name";

        when(rs.getString(column)).thenReturn("a banana");

        try{
            handler.getResult(rs, column);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getCause(), is(instanceOf(MalformedURLException.class)));
        }
    }

    @Test
    public void readsNullResultByName() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        String column = "column name";

        when(rs.getObject(column)).thenReturn(null);
        when(rs.wasNull()).thenReturn(true);

        URL result = handler.getResult(rs, column);

        assertThat(result, is(nullValue()));
    }

    @Test
    public void readsNonNullCallableResult() throws SQLException, MalformedURLException {
        CallableStatement cs = mock(CallableStatement.class);
        int index = 5;
        URL expected = new URL("http://www.example.com/");

        when(cs.getString(index)).thenReturn("http://www.example.com/");

        URL result = handler.getResult(cs, index);

        assertThat(result, is(expected));
    }

    @Test
    public void readsNonUrlCallableResult() throws SQLException {
        CallableStatement cs = mock(CallableStatement.class);
        int index = 5;

        when(cs.getString(index)).thenReturn("a banana");

        try{
            handler.getResult(cs, index);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getCause(), is(instanceOf(MalformedURLException.class)));
        }
    }

    @Test
    public void readsNullCallableResult() throws SQLException {
        CallableStatement cs = mock(CallableStatement.class);
        int index = 5;

        when(cs.getString(index)).thenReturn(null);
        when(cs.wasNull()).thenReturn(true);

        URL result = handler.getResult(cs, index);

        assertThat(result, is(nullValue()));
    }
}