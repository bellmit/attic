package com.unreasonent.ds.database;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.db.TimeBoundHealthCheck;
import io.dropwizard.util.Duration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class QueryHealthCheck extends HealthCheck {
    private final TimeBoundHealthCheck timeBound;
    private final DataSource dataSource;

    public QueryHealthCheck(ExecutorService executorService, Duration duration, DataSource dataSource) {
        this.timeBound = new TimeBoundHealthCheck(executorService, duration);
        this.dataSource = dataSource;
    }

    @Override
    protected Result check() throws Exception {
        return timeBound.check(this::queryCheck);
    }

    private Result queryCheck() {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT 1");
                ResultSet rs = ps.executeQuery()
        ) {
            if (!rs.next()) {
                return Result.unhealthy("no rows returned from SELECT 1");
            }

            int i = rs.getInt(1);
            if (i != 1) {
                return Result.unhealthy("Unexpected response %d from SELECT 1", i);
            }

            return Result.healthy();
        } catch (SQLException sqle) {
            return Result.unhealthy(sqle);
        }
    }
}
