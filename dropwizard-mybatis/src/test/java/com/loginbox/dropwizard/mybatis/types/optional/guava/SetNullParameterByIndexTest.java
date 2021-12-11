package com.loginbox.dropwizard.mybatis.types.optional.guava;

import com.google.common.base.Optional;
import com.loginbox.dropwizard.mybatis.types.GuavaOptionalTypeHandler;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class SetNullParameterByIndexTest {
    @Parameters(name = "setParameter(..., {0}, {1}) binds to setNull(..., {2})")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {Optional.absent(), JdbcType.VARCHAR, Types.VARCHAR},
                {Optional.absent(), JdbcType.INTEGER, Types.INTEGER},
                {Optional.absent(), null, Types.OTHER},
                {null, JdbcType.VARCHAR, Types.VARCHAR},
                {null, JdbcType.INTEGER, Types.INTEGER},
                {null, null, Types.OTHER},
        });
    }

    private final PreparedStatement ps = mock(PreparedStatement.class);
    private final GuavaOptionalTypeHandler handler = new GuavaOptionalTypeHandler();

    private final Optional<Object> domainValue;
    private final JdbcType jdbcType;
    private final int statementType;

    public SetNullParameterByIndexTest(Optional<Object> domainValue, JdbcType jdbcType, int statementType) {
        this.domainValue = domainValue;
        this.jdbcType = jdbcType;
        this.statementType = statementType;
    }

    @Test
    public void setsParametersAsExpected() throws SQLException {
        int index = 5;

        handler.setParameter(ps, index, domainValue, jdbcType);
        verify(ps).setNull(index, statementType);
    }
}
