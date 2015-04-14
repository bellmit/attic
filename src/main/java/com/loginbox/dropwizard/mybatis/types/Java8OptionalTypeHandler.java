package com.loginbox.dropwizard.mybatis.types;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Maps Java 8 {@link java.util.Optional}s to database values. This mapping uses {@link
 * java.sql.ResultSet#getObject(int) getObject} and {@link java.sql.PreparedStatement#setObject(int, Object, int)
 * setObject} to store the underlying value, which means the underlying JDBC driver needs to have a useful
 * implementation of those methods.
 */
public class Java8OptionalTypeHandler implements TypeHandler<Optional<Object>> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Optional<Object> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null && parameter.isPresent())
            ps.setObject(i, parameter.get(), jdbcType.TYPE_CODE);
        else
            ps.setNull(i, jdbcType.TYPE_CODE);
    }

    @Override
    public Optional<Object> getResult(ResultSet rs, String columnName) throws SQLException {
        Object rawValue = rs.getObject(columnName);
        return makeOptional(rawValue);
    }

    @Override
    public Optional<Object> getResult(ResultSet rs, int columnIndex) throws SQLException {
        Object rawValue = rs.getObject(columnIndex);
        return makeOptional(rawValue);
    }

    @Override
    public Optional<Object> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object rawValue = cs.getObject(columnIndex);
        return makeOptional(rawValue);
    }

    private Optional<Object> makeOptional(Object rawValue) {
        if (rawValue == null)
            return Optional.empty();
        return Optional.of(rawValue);
    }
}
