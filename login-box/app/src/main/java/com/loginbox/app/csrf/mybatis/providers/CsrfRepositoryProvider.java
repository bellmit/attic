package com.loginbox.app.csrf.mybatis.providers;

import com.loginbox.app.csrf.storage.CsrfRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import java.util.function.Supplier;

/**
 * Provides dependency injection of a CsrfRepository backed by a Mybatis SQL session.
 */
public class CsrfRepositoryProvider implements Factory<TransactedCsrfRepository> {
    public static class Binder extends AbstractBinder {
        private final CsrfRepositoryProvider provider;

        public Binder(CsrfRepositoryProvider provider) {
            this.provider = provider;
        }

        @Override
        protected void configure() {
            bindFactory(provider).to(CsrfRepository.class).in(RequestScoped.class);
        }
    }

    public static Binder binder(Supplier<SqlSessionFactory> sqlSessionFactorySupplier) {
        CsrfRepositoryProvider provider = new CsrfRepositoryProvider(sqlSessionFactorySupplier);
        Binder binder = new Binder(provider);
        return binder;
    }

    private final Supplier<SqlSessionFactory> sqlSessionFactorySupplier;

    public CsrfRepositoryProvider(Supplier<SqlSessionFactory> sqlSessionFactorySupplier) {
        this.sqlSessionFactorySupplier = sqlSessionFactorySupplier;
    }

    @Override
    public TransactedCsrfRepository provide() {
        SqlSessionFactory sqlSessionFactory = sqlSessionFactorySupplier.get();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        TransactedCsrfRepository csrfRepository = new TransactedCsrfRepository(sqlSession);
        return csrfRepository;
    }

    @Override
    public void dispose(TransactedCsrfRepository instance) {
        try (TransactedCsrfRepository closingInstance = instance) {
            closingInstance.commit();
        }
    }
}
