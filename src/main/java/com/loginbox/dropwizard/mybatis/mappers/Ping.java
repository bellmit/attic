package com.loginbox.dropwizard.mybatis.mappers;

/**
 * Provides a simple read-only query to test database connectivity. This query touches no tables.
 * <p>
 * By default, the mapper runs {@code SELECT 1} to verify that the database is reachable. Applications can override this
 * query by providing their own copy of the mapper configuration file {@code Ping.xml}, in this package. This may be
 * necessary on database management systems such as Oracle, which do not allow queries to omit the {@code FROM} clause.
 */
public interface Ping {
    /**
     * Pings the database.
     *
     * @return a meaningless value.
     */
    public int ping();
}
