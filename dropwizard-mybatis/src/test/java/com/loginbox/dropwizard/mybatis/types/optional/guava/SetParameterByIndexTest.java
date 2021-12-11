package com.loginbox.dropwizard.mybatis.types.optional.guava;

import com.loginbox.dropwizard.mybatis.types.GuavaOptionalTypeHandler;
import com.loginbox.dropwizard.mybatis.types.Java8OptionalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class SetParameterByIndexTest {
    @Parameters(name = "setParameter(..., {0}, {1}) binds to setObject(..., {2}, {3})")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {Optional.of("Hello"), JdbcType.VARCHAR, "Hello", Types.VARCHAR},
                {Optional.of(5), JdbcType.INTEGER, 5, Types.INTEGER},
                {Optional.of("Hello"), null, "Hello", Types.OTHER},
        });
    }

    private final PreparedStatement ps = mock(PreparedStatement.class);
    private final GuavaOptionalTypeHandler handler = new GuavaOptionalTypeHandler();

    private final Optional<Object> domainValue;
    private final JdbcType jdbcType;
    private final Object statementValue;
    private final int statementType;

    public SetParameterByIndexTest(Optional<Object> domainValue, JdbcType jdbcType, Object statementValue, int statementType) {
        this.domainValue = domainValue;
        this.jdbcType = jdbcType;
        this.statementValue = statementValue;
        this.statementType = statementType;
    }

    @Test
    public void setsParametersAsExpected() throws SQLException {
        int index = 5;

        handler.setParameter(ps, index, domainValue, jdbcType);
        verify(ps).setObject(index, statementValue, statementType);
    }
}
