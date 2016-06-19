package com.unreasonent.ds.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class Transact {
    public static void transact(SqlSessionFactory sqlSessionFactory, Consumer<SqlSession> transaction) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            transaction.accept(session);
            session.commit();
        }
    }

    public static <T> T transacted(SqlSessionFactory sqlSessionFactory, Function<SqlSession, T> transaction) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            T result = transaction.apply(session);
            session.commit();
            return result;
        }
    }
}
