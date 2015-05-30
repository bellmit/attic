package com.loginbox.app.transactor.mybatis;

import com.loginbox.transactor.adapter.Adapter;
import org.apache.ibatis.session.SqlSession;

/**
 * Provides adapters for working with Mybatis transactables.
 */
public class MybatisAdapters {
    private MybatisAdapters() {
    }

    /**
     * Provides adapters that convert transactables in terms of Mybatis mapper interfaces into transactables in terms of
     * {@link org.apache.ibatis.session.SqlSession}s.
     *
     * @param mapper
     *         the mapper interface type.
     * @param <M>
     *         the type of the mapper interface.
     * @return an adapter for <var>M</var>.
     */
    public static <M> Adapter<SqlSession, M> mapper(Class<M> mapper) {
        return session -> session.getMapper(mapper);
    }
}
