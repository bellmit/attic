package com.loginbox.app.transactor.mybatis;

import com.loginbox.transactor.Transactor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.function.Supplier;

/**
 * A transactor over a Mybatis {@link org.apache.ibatis.session.SqlSession}.
 */
public class MybatisTransactor extends Transactor<SqlSession> {
    private final Supplier<SqlSessionFactory> sqlSessionFactorySupplier;

    public MybatisTransactor(Supplier<SqlSessionFactory> sqlSessionFactorySupplier) {
        this.sqlSessionFactorySupplier = sqlSessionFactorySupplier;
    }

    @Override
    protected SqlSession createContext() {
        SqlSessionFactory sqlSessionFactory = sqlSessionFactorySupplier.get();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }

    @Override
    protected void abort(SqlSession context) {
        context.rollback();
    }

    @Override
    protected void finish(SqlSession context) {
        context.commit();
    }
}
